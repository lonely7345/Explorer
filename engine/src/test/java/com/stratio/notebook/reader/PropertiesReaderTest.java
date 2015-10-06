package com.stratio.notebook.reader;

import com.stratio.notebook.conf.ConstantsFolder;
import com.stratio.notebook.exceptions.FolderNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class PropertiesReaderTest {


    private static final String NOT_EXIST_PATH ="not_exit";
    private Properties result ;
    private PropertiesReader reader;


    @Before
    public void setUp(){
        result = new Properties();

        reader = new PropertiesReader();

    }


    @After
    public void tearDown(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION ="/conf";
    }


    @Test(expected = FolderNotFoundException.class)
    public void whenNotExistPath(){
        reader.readConfigFrom(NOT_EXIST_PATH);
    }

    @Test public void whenExistFileAndContainsData(){
        result.put("cassandra.host","127.0.0.1");
        result.put("cassandra.port","9042");
        assertThat(reader.readConfigFrom("cassandra"), is(result));
    }
}
