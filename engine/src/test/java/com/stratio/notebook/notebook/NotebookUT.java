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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.SchedulerException;

import com.stratio.notebook.conf.ExplorerConfiguration;
import com.stratio.notebook.conf.ExplorerConfiguration.ConfVars;
import com.stratio.notebook.interpreter.mock.MockInterpreterFactory;
import com.stratio.notebook.scheduler.Job;
import com.stratio.notebook.scheduler.JobListener;
import com.stratio.notebook.scheduler.SchedulerFactory;

public class NotebookUT implements JobListenerFactory{

	private File tmpDir;
	private ExplorerConfiguration conf;
	private SchedulerFactory schedulerFactory;
	private File notebookDir;
	private Notebook notebook;

	@Before
	public void setUp() throws Exception {
		tmpDir = new File(System.getProperty("java.io.tmpdir")+"/NoteBoTest_"+System.currentTimeMillis());
		tmpDir.mkdirs();
		notebookDir = new File(System.getProperty("java.io.tmpdir")+"/ExplorerLTest_"+System.currentTimeMillis()
				+"/explorer");
		notebookDir.mkdirs();

		System.setProperty(ConfVars.EXPLORER_NOTEBOOK_DIR.getVarName(), notebookDir.getAbsolutePath());

		conf = ExplorerConfiguration.create();
        
		this.schedulerFactory = new SchedulerFactory();
		
		notebook = new Notebook(conf, schedulerFactory, new MockInterpreterFactory(conf), this);
	}

	@After
	public void tearDown() throws Exception {
		delete(tmpDir);
	}

	@Test
	public void testSelectingReplImplementation() {
		Note note = notebook.createNote();
		
		// run with default repl
		Paragraph p1 = note.addParagraph();
		p1.setText("hello world");
		note.run(p1.getId());
		while(p1.isTerminated()==false || p1.getResult()==null) Thread.yield();
		assertEquals("repl1: hello world", p1.getResult().message());
		
		// run with specific repl
		Paragraph p2 = note.addParagraph();
		p2.setText("%MockRepl2 hello world");
		note.run(p2.getId());
		while(p2.isTerminated()==false || p2.getResult()==null) Thread.yield();
		assertEquals("repl2: hello world", p2.getResult().message());
	}
	
	@Test
	public void testPersist() throws IOException, SchedulerException{
		Note note = notebook.createNote();
		
		// run with defatul repl
		Paragraph p1 = note.addParagraph();
		p1.setText("hello world");
		note.persist();
		
		Notebook notebook2 = new Notebook(conf, schedulerFactory, new MockInterpreterFactory(conf), this);
		assertEquals(1, notebook2.getAllNotes().size());
	}
	
	@Test
	public void testRunAll() {
		Note note = notebook.createNote();
		Paragraph p1 = note.addParagraph();
		p1.setText("p1");
		Paragraph p2 = note.addParagraph();
		p2.setText("p2");
		assertEquals(null, p2.getResult());
		note.runAll();
		
		while(p2.isTerminated()==false || p2.getResult()==null) Thread.yield();
		assertEquals("repl1: p2", p2.getResult().message());
	}
	
	@Test
	public void testSchedule() throws InterruptedException{
		// create a note and a paragraph
		Note note = notebook.createNote();
		Paragraph p = note.addParagraph();
		p.setText("p1");
		Date dateFinished = p.getDateFinished();
		assertNull(dateFinished);
		
		// set cron scheduler, once a second
		Map<String, Object> config = note.getConfig();
		config.put("cron", "* * * * * ?");
		note.setConfig(config);
		notebook.refreshCron(note.id());
		Thread.sleep(1*1000);
		dateFinished = p.getDateFinished();
		assertNotNull(dateFinished);
		
		// remove cron scheduler.
		config.put("cron", null);
		note.setConfig(config);
		notebook.refreshCron(note.id());
		Thread.sleep(1*1000);
		Assert.assertEquals(dateFinished, p.getDateFinished());
	}
	
	private void delete(File file){
		if(file.isFile()) file.delete();
		else if(file.isDirectory()){
			File [] files = file.listFiles();
			if(files!=null && files.length>0){
				for(File f : files){
					delete(f);
				}
			}
			file.delete();
		}
	}

	@Override
	public JobListener getParagraphJobListener(Note note) {
		return new JobListener(){

			@Override
			public void onProgressUpdate(Job job, int progress) {
			}

			@Override
			public void beforeStatusChange(Job job, Job.Status before, Job.Status after) {
			}

			@Override
			public void afterStatusChange(Job job, Job.Status before, Job.Status after) {
			}			
		};
	}
}
