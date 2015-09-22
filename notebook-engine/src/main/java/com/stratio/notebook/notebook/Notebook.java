/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.notebook.notebook;

import com.stratio.notebook.conf.ZeppelinConfiguration;
import com.stratio.notebook.interpreter.InterpreterFactory;
import com.stratio.notebook.scheduler.Scheduler;
import com.stratio.notebook.scheduler.SchedulerFactory;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Consist of Notes
 */
public class Notebook {
    Logger logger = LoggerFactory.getLogger(Notebook.class);

    private SchedulerFactory schedulerFactory;
    private InterpreterFactory replFactory;
    /**
     * Keep the order
     */
    Map<String, Note> notes = new LinkedHashMap<String, Note>();

    private ZeppelinConfiguration conf;

    private StdSchedulerFactory quertzSchedFact;

    private org.quartz.Scheduler quartzSched;

    private JobListenerFactory jobListenerFactory;

    public Notebook(ZeppelinConfiguration conf, SchedulerFactory schedulerFactory, InterpreterFactory replFactory,
            JobListenerFactory jobListenerFactory) throws IOException, SchedulerException {
        this.conf = conf;
        this.schedulerFactory = schedulerFactory;
        this.replFactory = replFactory;
        this.jobListenerFactory = jobListenerFactory;
        quertzSchedFact = new org.quartz.impl.StdSchedulerFactory();
        quartzSched = quertzSchedFact.getScheduler();
        quartzSched.start();
        CronJob.notebook = this;

        loadAllNotes();
    }

    private boolean isLoaderStatic() {
        return "share".equals(conf.getString(ZeppelinConfiguration.ConfVars.NOTEBOOK_INTERPRETER_MODE));
    }

    /**
     * Create new note
     *
     * @return
     */
    public Note createNote() {
        Note note = new Note(conf, new NoteInterpreterLoader(replFactory, isLoaderStatic()), jobListenerFactory,
                quartzSched);
        synchronized (notes) {
            notes.put(note.id(), note);
        }
        return note;
    }

    public Note importFromFile(String filename, String path) throws IOException {

        Note note = Note.importFromFile(new Note(conf, new NoteInterpreterLoader(replFactory, isLoaderStatic()), jobListenerFactory,
                quartzSched),filename, path);
        synchronized (notes) {
            if (note != null) {
                notes.put(note.id(), note);
            }
        }
        return note;

    }

    public Note getNote(String id) {
        synchronized (notes) {
            return notes.get(id);
        }
    }

    public void removeNote(String id) {
        Note note;
        synchronized (notes) {
            note = notes.remove(id);
        }
        if (note.getNoteReplLoader() != null) {
            note.getNoteReplLoader().destroyAll();
        }
        try {
            note.unpersist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllNotes() throws IOException {
        File notebookDir = new File(conf.getNotebookDir());
        File[] dirs = notebookDir.listFiles();
        if (dirs == null) {
            return;
        }
        for (File f : dirs) {
            boolean isHidden = !f.getName().startsWith(".");
            if (f.isDirectory() && isHidden) {
                Scheduler scheduler = schedulerFactory.createOrGetFIFOScheduler("note_" + System.currentTimeMillis());
                logger.info("Loading note from " + f.getName());
                Note note = Note
                        .load(f.getName(), conf, new NoteInterpreterLoader(replFactory, isLoaderStatic()), scheduler,
                                jobListenerFactory, quartzSched);
                synchronized (notes) {
                    notes.put(note.id(), note);
                    refreshCron(note.id());
                }
            }
        }
    }

    public List<Note> getAllNotes() {
        synchronized (notes) {
            List<Note> noteList = new ArrayList<Note>(notes.values());
            logger.info("" + noteList.size());
            Collections.sort(noteList, new Comparator() {
                @Override
                public int compare(Object one, Object two) {
                    Note note1 = (Note) one;
                    Note note2 = (Note) two;

                    String name1 = note1.id();
                    if (note1.getName() != null) {
                        name1 = note1.getName();
                    }
                    String name2 = note2.id();
                    if (note2.getName() != null) {
                        name2 = note2.getName();
                    }
                    ((Note) one).getName();
                    return name1.compareTo(name2);
                }
            });
            return noteList;
        }
    }

    public JobListenerFactory getJobListenerFactory() {
        return jobListenerFactory;
    }

    public void setJobListenerFactory(JobListenerFactory jobListenerFactory) {
        this.jobListenerFactory = jobListenerFactory;
    }

    public static class CronJob implements org.quartz.Job {
        public static Notebook notebook;

        @Override
        public void execute(JobExecutionContext context)
                throws JobExecutionException {

            String noteId = context.getJobDetail().getJobDataMap().getString("noteId");
            Note note = notebook.getNote(noteId);
            note.runAll();
        }
    }

    public void refreshCron(String id) {
        removeCron(id);
        synchronized (notes) {

            Note note = notes.get(id);
            if (note == null) {
                return;
            }
            Map<String, Object> config = note.getConfig();
            if (config == null) {
                return;
            }

            String cronExpr = (String) note.getConfig().get("cron");
            if (cronExpr == null || cronExpr.trim().length() == 0) {
                return;
            }

            JobDetail newJob = JobBuilder.newJob(CronJob.class)
                    .withIdentity(id, "note")
                    .usingJobData("noteId", id)
                    .build();

            Map<String, Object> info = note.getInfo();
            info.put("cron", null);

            CronTrigger trigger = null;
            try {
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity("trigger_" + id, "note")
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
                        .forJob(id, "note")
                        .build();
            } catch (Exception e) {
                logger.error("Error", e);
                info.put("cron", e.getMessage());
            }

            try {
                if (trigger != null) {
                    quartzSched.scheduleJob(newJob, trigger);
                }
            } catch (SchedulerException e) {
                logger.error("Error", e);
                info.put("cron", "Scheduler Exception");
            }
        }
    }

    private void removeCron(String id) {
        try {
            quartzSched.deleteJob(new JobKey(id, "note"));
        } catch (SchedulerException e) {
            logger.error("Can't remove quertz " + id, e);
        }
    }

}
