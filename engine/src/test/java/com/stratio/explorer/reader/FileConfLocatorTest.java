package com.stratio.explorer.reader;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by afidalgo on 26/10/15.
 */
public class FileConfLocatorTest {

    private FileConfLocator locator;

    @Before
    public void setUp(){
        locator = new FileConfLocator();
    }

    @Test
    public void whenFileExistInSameFolder(){
        String filePath = locator.locate("test_file.conf");
        assertTrue("when locate file then return path",filePath.endsWith("test-classes/test_file.conf"));
    }


    @Test
    public void whenFileExistIndistinctFolder(){
        String filePath = locator.locate("distinct_folder.conf");
        assertTrue("when locate file then return path", filePath.endsWith("test/java/com/stratio/explorer/reader/distinct_folder.conf"));
    }


    @Test
    public void whenFileNotexist(){
        String filePath = locator.locate("NO_FILE_SKHASGFTF.conf");
        assertThat("When file not exist then return emptyString", filePath,is(""));
    }
}
