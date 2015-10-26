package com.stratio.explorer.notebook;

import com.stratio.explorer.conf.ExplorerConfiguration;
import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.scheduler.Job;
import com.stratio.explorer.scheduler.JobListener;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import org.quartz.Scheduler;


import java.io.File;
import java.util.Date;
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
        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);
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
        assertEquals("The number of paragraph is correct", numberOfParagraph + 1, note.paragraphs.size());



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
        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);

        com.stratio.explorer.scheduler.Scheduler shedule = mock(com.stratio.explorer.scheduler.Scheduler.class);
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

        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);

        com.stratio.explorer.scheduler.Scheduler shedule = mock(com.stratio.explorer.scheduler.Scheduler.class);
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

        assertNotSame("The list must not be the same", paragraphList, returnParagraph);
        assertEquals("The two list have the same size", paragraphList.size(), returnParagraph.size());
        for (Paragraph p: returnParagraph){
            assertTrue("The two list have the same paragraph", paragraphList.contains(p));
        }
    }

    @Test
    public void testExportToFile() throws Exception {


        String basePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator;

        File f = new File(basePath + "Test.json");
        if (f.exists()){
            f.delete();
        }

        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);
        expect(conf.getString(ExplorerConfiguration.ConfVars.EXPLORER_ENCODING)).andReturn("UTF-8");
        replay(conf);
        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);

        Scheduler quartzShed =  mock(Scheduler.class);
        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);
        note.setName("TEST_NOTE");
        Whitebox.setInternalState(note, "id", "2AZE9X5GG");
        Whitebox.setInternalState(note, "creationDate", "lun, 5 oct, 10:10 AM");

        JobListener jobListener = mock(JobListener.class);
        jobListener.beforeStatusChange(anyObject(Job.class), eq(com.stratio.explorer.scheduler.Job.Status.READY), eq(com.stratio.explorer.scheduler.Job.Status.FINISHED));
        EasyMock.expectLastCall();
        Paragraph paragraph = new Paragraph(jobListener, replLoader);
        paragraph.setTitle("PARAGRAPH_TITLE");
        Whitebox.setInternalState(paragraph, "dateCreated", new Date(1000000));
        Whitebox.setInternalState(paragraph, "jobName", "paragraph_1444033271219_-1226681848");


        List<Paragraph> paragraphList = new LinkedList<>();
        paragraphList.add(paragraph);

        note.addParagraphs(paragraphList);

        Whitebox.setInternalState(paragraph, "id", "20151005-095152_2104354711");

        replay(jobListener);
        note.exportToFile(basePath, "Test");


        assertTrue("The file must Exist", f.exists());
        File fileExpected = new File(basePath+"Test_Export_default.json");
        assertEquals("The file must be the correct content",FileUtils.readLines(fileExpected),FileUtils.readLines(f));


         f.delete(); //We clean the enviroment


    }

    @Test
    public void testPersist() throws Exception {
        String note_id = "2AZE9X5GG";
        String basePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator;

        File f = new File(basePath + File.separator+note_id +File.separator+ "note.json");
        if (f.exists()){
            f.delete();
        }


        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);
        expect(conf.getString(ExplorerConfiguration.ConfVars.EXPLORER_ENCODING)).andReturn("UTF-8");
        expect(conf.getExplorerDir()).andReturn(basePath);
        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);

        Scheduler quartzShed =  mock(Scheduler.class);
        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);

        Whitebox.setInternalState(note, "id", note_id);
        Whitebox.setInternalState(note, "creationDate", "lun, 5 oct, 10:35 AM");

        replay(conf);
        note.persist();

        assertTrue("The file must Exist", f.exists());
        File fileExpected = new File(basePath+File.separator+note_id+File.separator+"note_persist_default.json");
        assertEquals("The file must be the correct content",FileUtils.readLines(fileExpected),FileUtils.readLines(f));

        f.delete(); //We clean the enviroment

    }

    @Test
    public void testUnpersist() throws Exception {

        String basePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
        String note_id = "2AZE9X5GCC";

        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);
        expect(conf.getString(ExplorerConfiguration.ConfVars.EXPLORER_ENCODING)).andReturn("UTF-8");
        expect(conf.getExplorerDir()).andReturn(basePath);
        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);

        Scheduler quartzShed =  mock(Scheduler.class);
        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);
        Whitebox.setInternalState(note, "id", note_id);

        File createDirectory = new File(basePath+File.separator+note_id);
        createDirectory.mkdir();

        replay(conf);
        note.unpersist();

        assertFalse("The directory must not exits", createDirectory.exists());
    }

    @Test
    public void testImportFromFile() throws Exception {
        String basePath = "src" + File.separator + "test" + File.separator + "resources" + File.separator;
        String note_id = "2AZE9X5GCC";

        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);
        expect(conf.getString(ExplorerConfiguration.ConfVars.EXPLORER_ENCODING)).andReturn("UTF-8");
        expect(conf.getExplorerDir()).andReturn(basePath);
        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);

        Scheduler quartzShed =  mock(Scheduler.class);
        Note note = new Note(conf,replLoader,jobListenerFactory,quartzShed);
        Whitebox.setInternalState(note, "id", note_id);

        note.importFromFile(note, "Test_Export_default.json", basePath + File.separator + "Test_Export_default.json");

        assertEquals("The note name must be correct", "TEST_NOTE - copy", note.getName());
        List<Paragraph> paragraphs = note.getParagraphs();
        assertEquals("The paragraphNumber must be correct", 1, paragraphs.size());


    }

    @Test
    public void testLoad() throws Exception {
        String basePath = "src" + File.separator + "test";
        String note_id = "resources";

        ExplorerConfiguration conf = mock(ExplorerConfiguration.class);
        expect(conf.getString(ExplorerConfiguration.ConfVars.EXPLORER_ENCODING)).andReturn("UTF-8");
        expect(conf.getExplorerDir()).andReturn(basePath);
        NoteInterpreterLoader replLoader = mock(NoteInterpreterLoader.class);
        JobListenerFactory jobListenerFactory = mock(JobListenerFactory.class);

        Scheduler quartzShed =  mock(Scheduler.class);

        com.stratio.explorer.scheduler.Scheduler scheduler = mock(com.stratio.explorer.scheduler.Scheduler.class);
        replay(conf);
        Note note = Note.load(note_id,conf,replLoader,scheduler,jobListenerFactory,quartzShed);

        assertEquals("The note name must be correct", "TEST_NOTE", note.getName());
        List<Paragraph> paragraphs = note.getParagraphs();
        assertEquals("The paragraphNumber must be correct", 1, paragraphs.size());
    }



    private List<Paragraph> createParagraphList(int number) {
        List<Paragraph> paragraphList = new LinkedList();
        for (int i=0;i<number;i++) {
            paragraphList.add(new Paragraph(null, null));
        }
        return paragraphList;
    }


}