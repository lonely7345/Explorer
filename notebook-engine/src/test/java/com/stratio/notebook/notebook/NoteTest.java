/**
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

import com.stratio.notebook.interpreter.Interpreter;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.quartz.Scheduler;


import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;


/**
 * Created by jmgomez on 1/10/15.
 */
public class NoteTest {

    @Test
    public void testNoteWithParams(){
        ZeppelinConfiguration conf = mock(ZeppelinConfiguration.class);
        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);
        Scheduler quartzShed =  mock(Scheduler.class);

        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);

        assertSame("the conf must be correct", conf,Whitebox.getInternalState(note, "conf"));
        assertSame("the repLoader must be correct", replLoader,Whitebox.getInternalState(note, "replLoader"));
        assertSame("the jobListenerFactory must be correct", jobListenerFactory,Whitebox.getInternalState(note, "jobListenerFactory"));
    }

    @Test
    public void addParagraphsList(){
        Note note = new Note();
        List<Paragraph> paragraphList = createParagraphList(3);
        note.addParagraphs(paragraphList);

        assertEquals("The list must be the same paragragps", paragraphList.size(), note.paragraphs.size());
    }


    @Test
    public void addParagraphsListNull(){
        Note note = new Note();
        note.addParagraphs(null);

        assertEquals("The list must be the same paragragps", 0, note.paragraphs.size());
    }



    @Test
    public void testInsertParagraph() throws Exception {

        Note note = new Note();
        note.addParagraphs(createParagraphList(3));
        int index = 1;
        Paragraph paragrapReturn = note.insertParagraph(index);

        assertSame("The paragraph is the same", paragrapReturn, note.paragraphs.get(index));

    }


    @Test
    public void testInsertParagraphText() throws Exception {
        Note note = new Note();
        note.addParagraphs(createParagraphList(3));
        int index = 1;
        String text = "text";
        Paragraph paragrapReturn = note.insertParagraph(index,text);

        assertSame("The paragraph is the same", paragrapReturn, note.paragraphs.get(1));
        assertSame("The paragraph text is the same", text, note.paragraphs.get(1).getText());
    }

    @Test
    public void testRemoveParagraph() throws Exception {
        Note note = new Note();
        int numberOfParagraph = 3;
        note.addParagraphs(createParagraphList(numberOfParagraph));
        Paragraph paragraph = new Paragraph(null, null);
        List<Paragraph> paragraphList = new LinkedList<>();
        paragraphList.add(paragraph);
        note.addParagraphs(paragraphList);

        assertSame("The paragraph remove must be correct", paragraph, note.removeParagraph(paragraph.getId()));
        assertEquals("The number of paragraph is correct", numberOfParagraph, note.paragraphs.size());

    }

    @Test
    public void testMoveParagraph() throws Exception {

        Note note = new Note();
        int numberOfParagraph = 3;
        note.addParagraphs(createParagraphList(numberOfParagraph));
        Paragraph paragraph = new Paragraph(null, null);
        List<Paragraph> paragraphList = new LinkedList<>();
        paragraphList.add(paragraph);
        note.addParagraphs(paragraphList);

        note.moveParagraph(paragraph.getId(), 2);

        assertSame("The paragraph remove must be correct", paragraph, note.paragraphs.get(2));
        assertEquals("The number of paragraph is correct", numberOfParagraph+1, note.paragraphs.size());



    }

    @Test
    public void testIsLastParagraph() throws Exception {
        Note note = new Note();
        int numberOfParagraph = 3;
        List<Paragraph> firstParagraphList = createParagraphList(numberOfParagraph);
        note.addParagraphs(firstParagraphList);
        Paragraph paragraph = new Paragraph(null, null);
        List<Paragraph> paragraphList = new LinkedList<>();
        paragraphList.add(paragraph);
        note.addParagraphs(paragraphList);

        assertTrue("The las paragraph must be correct", note.isLastParagraph(paragraph.getId()));
        assertFalse("The las paragraph must not be the last.", note.isLastParagraph(firstParagraphList.get(0).getId()));
    }

    @Test
    public void testGetParagraph() throws Exception {
        Note note = new Note();
        int numberOfParagraph = 3;
        note.addParagraphs(createParagraphList(numberOfParagraph));
        Paragraph paragraph = new Paragraph(null, null);
        List<Paragraph> paragraphList = new LinkedList<>();
        paragraphList.add(paragraph);
        note.addParagraphs(paragraphList);

        assertSame("The paragraphs must be same", paragraph, note.getParagraph(paragraph.getId()));
    }

    @Test
    public void testGetLastParagraph() throws Exception {
        Note note = new Note();
        int numberOfParagraph = 3;
        note.addParagraphs(createParagraphList(numberOfParagraph));
        Paragraph paragraph = new Paragraph(null, null);
        List<Paragraph> paragraphList = new LinkedList<>();
        paragraphList.add(paragraph);
        note.addParagraphs(paragraphList);

        assertSame("The paragraphs must be same", paragraph, note.getLastParagraph());
    }

    @Test
    public void testRunAll() throws Exception {
        ZeppelinConfiguration conf = mock(ZeppelinConfiguration.class);

        com.stratio.notebook.scheduler.Scheduler shedule = mock(com.stratio.notebook.scheduler.Scheduler.class);
        shedule.submit(anyObject(Paragraph.class));
        expectLastCall().times(3);
        replay(shedule);

        Interpreter interpreter = mock(Interpreter.class);
        expect(interpreter.getScheduler()).andReturn(shedule);
        expectLastCall().times(3);
        replay(interpreter);

        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        expect(replLoader.getRepl(anyString())).andReturn(interpreter);
        expectLastCall().times(3);
        replay(replLoader);


        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);
        expect(jobListenerFactory.getParagraphJobListener(anyObject(Note.class))).andReturn(null);
        expectLastCall().times(3);
        replay(jobListenerFactory);

        Scheduler quartzShed =  mock(Scheduler.class);



        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);

        int numberOfParagraph = 3;
        note.addParagraphs(createParagraphList(numberOfParagraph));

        note.runAll();


        verify(shedule);

    }

    @Test
    public void testRun() throws Exception {

        ZeppelinConfiguration conf = mock(ZeppelinConfiguration.class);

        com.stratio.notebook.scheduler.Scheduler shedule = mock(com.stratio.notebook.scheduler.Scheduler.class);
        shedule.submit(anyObject(Paragraph.class));

        replay(shedule);

        Interpreter interpreter = mock(Interpreter.class);
        expect(interpreter.getScheduler()).andReturn(shedule);

        replay(interpreter);

        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        expect(replLoader.getRepl(anyString())).andReturn(interpreter);

        replay(replLoader);


        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);
        expect(jobListenerFactory.getParagraphJobListener(anyObject(Note.class))).andReturn(null);

        replay(jobListenerFactory);

        Scheduler quartzShed =  mock(Scheduler.class);



        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);

        int numberOfParagraph = 3;
        List<Paragraph> paragraphList = createParagraphList(numberOfParagraph);
        note.addParagraphs(paragraphList);

        note.run(paragraphList.get(0).getId());


        verify(shedule);
    }

    @Test
    public void testGetParagraphs() throws Exception {
        Note note = new Note();
        int numberOfParagraph = 3;
        List<Paragraph> paragraphList = createParagraphList(numberOfParagraph);
        note.addParagraphs(paragraphList);


        List<Paragraph> returnParagraph = note.getParagraphs();

        assertNotSame("The list must not be the same",paragraphList,returnParagraph);
        assertEquals("The two list have the same size",paragraphList.size(),returnParagraph.size());
        for (Paragraph p: returnParagraph){
            assertTrue("The two list have the same paragraph", paragraphList.contains(p));
        }
    }

    @Test
    public void testExportToFile() throws Exception {

    }

    @Test
    public void testPersist() throws Exception {

    }

    @Test
    public void testUnpersist() throws Exception {

    }

    @Test
    public void testImportFromFile() throws Exception {

    }

    @Test
    public void testLoad() throws Exception {

    }

    @Test
    public void testonProgressUpdate(){

    }
    @Test
    public void tefbeforeStatusChange(){

    }
    @Test
    public void testAfterStatusChange(){

    }

    private List<Paragraph> createParagraphList(int number) {
        List<Paragraph> paragraphList = new LinkedList();
        for (int i=0;i<number;i++) {
            paragraphList.add(new Paragraph(null, null));
        }
        return paragraphList;
    }


}