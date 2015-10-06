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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stratio.notebook.conf.ExplorerConfiguration;
import com.stratio.notebook.conf.ExplorerConfiguration.ConfVars;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.notebook.utility.IdHashes;
import com.stratio.notebook.scheduler.Job;
import com.stratio.notebook.scheduler.JobListener;
import com.stratio.notebook.scheduler.Scheduler;

/**
 * Consist of Paragraphs with independent context
 */
public class Note implements Serializable, JobListener {
    /**
     * The log.
     */
    transient static Logger logger = LoggerFactory.getLogger(Note.class);
    /**
     * A list of paragraphs.
     */
    List<Paragraph> paragraphs = new LinkedList<Paragraph>();
    private String name;
    private String id;
    private String creationDate;

    private transient NoteInterpreterLoader replLoader;
    private transient ExplorerConfiguration conf;
    private transient JobListenerFactory jobListenerFactory;

    /**
     * note configurations
     * <p/>
     * - looknfeel
     * - cron
     */
    private Map<String, Object> config = new HashMap<String, Object>();

    /**
     * note information
     * <p/>
     * - cron : cron expression validity.
     */
    private Map<String, Object> info = new HashMap<String, Object>();

    /**
     * Constructor.
     */
    public Note() {
    }

    public Note(ExplorerConfiguration conf, NoteInterpreterLoader replLoader, JobListenerFactory jobListenerFactory,
            org.quartz.Scheduler quartzSched) { //TODO  quartzSched is not ussing
        this.conf = conf;
        this.replLoader = replLoader;
        this.jobListenerFactory = jobListenerFactory;
        generateId();
        this.creationDate = new SimpleDateFormat("EEE, d MMM, HH:mm a").format(new Date(System.currentTimeMillis()));
    }

    private void generateId() {
        id = IdHashes.encode(System.currentTimeMillis() + new Random(System.currentTimeMillis()).nextInt());
    }

    public String id() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NoteInterpreterLoader getNoteReplLoader() {
        return replLoader;
    }

    public void setReplLoader(NoteInterpreterLoader replLoader) {
        this.replLoader = replLoader;
    }

    public void setZeppelinConfiguration(ExplorerConfiguration conf) {
        this.conf = conf;
    }

    /**
     * Add paragraph.
     *
     */
    public Paragraph addParagraph() {
        Paragraph p = new Paragraph(this, replLoader);
        synchronized (paragraphs) {
            paragraphs.add(p);
        }
        return p;
    }

    /**
     * Add a list of paragraph.
     * @param paragraphList the list of paragraph.
     * @return paragraphList.
     */
    public List<Paragraph> addParagraphs(List<Paragraph> paragraphList) {
        if (paragraphList!=null) {
            synchronized (paragraphs) {
                paragraphs.addAll(paragraphList);
            }
        }
        return paragraphList;
    }

    /**
     * Insert paragraph in given index
     *
     * @param index the position where the paragraph will be inserted.
     * @return  a paragraph.
     */
    public Paragraph insertParagraph(int index) {
        Paragraph p = new Paragraph(this, replLoader);
        synchronized (paragraphs) {
            paragraphs.add(index, p);
        }
        return insertParagraph(index,null);
    }

    public Paragraph insertParagraph(int index, String text) {
        Paragraph p = null;
            if (index >= 0 && index < paragraphs.size()) {


            p = new Paragraph(this, replLoader);
            p.setText(text);
            synchronized (paragraphs) {
                paragraphs.add(index, p);
            }
        }
        return p;
    }

    /**
     * Remove paragraph by id
     *
     * @param paragraphId the paragraphId.
     * @return the paragraph removed.
     */
    public Paragraph removeParagraph(String paragraphId) {
        synchronized (paragraphs) {
            for (int i = 0; i < paragraphs.size(); i++) {
                Paragraph p = paragraphs.get(i);
                if (p.getId().equals(paragraphId)) {
                    paragraphs.remove(i);
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Move paragraph into the new index (order from 0 ~ n-1)
     *
     * @param paragraphId the paragraphId
     * @param index       new index
     */
    public void moveParagraph(String paragraphId, int index) {
        synchronized (paragraphs) {
            int oldIndex = -1;
            Paragraph p = null;

            if (index < 0 || index >= paragraphs.size()) {
                return;
            }

            for (int i = 0; i < paragraphs.size(); i++) {
                if (paragraphs.get(i).getId().equals(paragraphId)) {
                    oldIndex = i;
                    if (oldIndex == index) {
                        return;
                    }
                    p = paragraphs.remove(i);
                }
            }

            if (p == null) {
                return;
            } else {
                if (oldIndex < index) {
                    paragraphs.add(index, p);
                } else {
                    paragraphs.add(index, p);
                }
            }
        }
    }

    /**
     * Ask if the paragraph is the last.
     * @param paragraphId the paragraphId.
     * @return tru if the paragraph is the last. False in other case.
     */
    public boolean isLastParagraph(String paragraphId) {
        if (!paragraphs.isEmpty()) {
            synchronized (paragraphs) {
                if (paragraphId.equals(paragraphs.get(paragraphs.size() - 1).getId())) {
                    return true;
                }
            }
            return false;
        }
        /** because empty list, cannot remove nothing right? */  //TODO It is note sense, why if the list is empty we return false. Maybe the cause is in other code piece.
        return true;
    }

    /**
     * Return a paragraph by id.
     * @param paragraphId the paragraph id.
     * @return the paragraph.
     */
    public Paragraph getParagraph(String paragraphId) {
        synchronized (paragraphs) {
            for (Paragraph p : paragraphs) {
                if (p.getId().equals(paragraphId)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Return the last paragraph.
     * @return the last paragraph.
     */
    public Paragraph getLastParagraph() {
        synchronized (paragraphs) {
            return paragraphs.get(paragraphs.size() - 1);
        }
    }

    /**
     * Run all paragraphs sequentially.
     *
     */
    public void runAll() {
        synchronized (paragraphs) {
            for (Paragraph p : paragraphs) {
                p.setNoteReplLoader(replLoader);
                p.setListener(jobListenerFactory.getParagraphJobListener(this));
                Interpreter intp = replLoader.getRepl(p.getRequiredReplName());
                intp.getScheduler().submit(p);
            }
        }
    }

    /**
     * Run a single paragraph
     *
     * @param paragraphId
     */
    public void run(String paragraphId) {
        Paragraph p = getParagraph(paragraphId);
        p.setNoteReplLoader(replLoader);
        p.setListener(jobListenerFactory.getParagraphJobListener(this));
        Interpreter intp = replLoader.getRepl(p.getRequiredReplName());    //TODO set a replloader from paragraph config
        intp.getScheduler().submit(p);
    }

    /**
     * Return a new list with all the paragraph.
     * @return a new list with all the paragraph.
     */
    public List<Paragraph> getParagraphs() {
        synchronized (paragraphs) {
            return new LinkedList<Paragraph>(paragraphs);
        }
    }

    public void exportToFile(String path, String filename) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        } else if (dir.isFile()) {
            throw new RuntimeException("File already exists" + dir.toString());
        }

        File file = new File(dir.getPath() + "/" + filename + ".json");
        logger.info("Persist note {} into {}", filename, file.getAbsolutePath());

        String json = gson.toJson(this);
        FileOutputStream out = new FileOutputStream(file);
        out.write(json.getBytes(conf.getString(ConfVars.EXPLORER_ENCODING)));
        out.close();
    }

    public void persist() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        if (conf == null) {
            conf = ExplorerConfiguration.create();
        }
        String explorerDir = conf.getExplorerDir();
        File dir = new File(explorerDir + "/" + id);
        if (!dir.exists()) {
            dir.mkdirs();
        } else if (dir.isFile()) {
            throw new RuntimeException("File already exists" + dir.toString());
        }

        File file = new File(explorerDir + "/" + id + "/note.json");
        logger.info("Persist note {} into {}", id, file.getAbsolutePath());

        String json = gson.toJson(this);
        FileOutputStream out = new FileOutputStream(file);
        out.write(json.getBytes(conf.getString(ConfVars.EXPLORER_ENCODING)));
        out.close();
    }

    public void unpersist() throws IOException {
        File dir = new File(conf.getExplorerDir() + "/" + id);

        FileUtils.deleteDirectory(dir);
    }

    public static Note importFromFile(Note n, String filename, String path) throws
            IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        File file = new File(path);
        logger.info("Load note {} from {}", filename, file.getAbsolutePath());

        if (!file.isFile()) {
            logger.info("##### Note-> !file found");
            return null;
        }

        String[] filenameWithExtension = filename.split("\\.");
        for (String aFilenameWithExtension : filenameWithExtension) {
            logger.info("##### Note-> " + aFilenameWithExtension);
        }
        String ext = filenameWithExtension[filenameWithExtension.length - 1];
        FileInputStream ins = new FileInputStream(file);
        String fileString = IOUtils.toString(ins, ConfVars.EXPLORER_ENCODING.getStringValue());
        if (ext.compareToIgnoreCase("json") == 0) {
            Note note = gson.fromJson(fileString, Note.class);
            n.addParagraphs(note.getParagraphs());
            n.setName(note.getName() + " - copy");
            for (Paragraph p : n.paragraphs) {
                if (p.getStatus() == Job.Status.PENDING || p.getStatus() == Job.Status.REFRESH_RESULT
                        || p.getStatus() == Job.Status.RUNNING) {
                    p.setStatus(Job.Status.ABORT);
                }
            }
        } else {
            String[] lines = fileString.split("\\r?\\n");
            for (String line : lines) {
                Paragraph p = n.addParagraph();
                p.setText(line);
            }
        }

        return n;
    }

    public static Note load(String id, ExplorerConfiguration conf, NoteInterpreterLoader replLoader,
            Scheduler scheduler, JobListenerFactory jobListenerFactory, org.quartz.Scheduler quartzSched)
            throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        File file = new File(conf.getExplorerDir() + "/" + id + "/note.json");
        logger.info("Load note {} from {}", id, file.getAbsolutePath());

        if (!file.isFile()) {
            return null;
        }

        FileInputStream ins = new FileInputStream(file);
        String json = IOUtils.toString(ins, conf.getString(ConfVars.EXPLORER_ENCODING));
        Note note = gson.fromJson(json, Note.class);
        note.setZeppelinConfiguration(conf);
        note.setReplLoader(replLoader);
        note.jobListenerFactory = jobListenerFactory;
        for (Paragraph p : note.paragraphs) {
            if (p.getStatus() == Job.Status.PENDING || p.getStatus() == Job.Status.REFRESH_RESULT || p.getStatus() == Job.Status
                    .RUNNING) {
                p.setStatus(Job.Status.ABORT);
            }
        }

        return note;
    }

    public Map<String, Object> getConfig() {
        if (config == null) {
            config = new HashMap<String, Object>();
        }
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getInfo() {
        if (info == null) {
            info = new HashMap<String, Object>();
        }
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    @Override
    public void beforeStatusChange(Job job, Job.Status before, Job.Status after) {
        Paragraph p = (Paragraph) job;
    }

    @Override
    public void afterStatusChange(Job job, Job.Status before, Job.Status after) {
        Paragraph p = (Paragraph) job;
    }



    @Override
    public void onProgressUpdate(Job job, int progress) {
    }

}
