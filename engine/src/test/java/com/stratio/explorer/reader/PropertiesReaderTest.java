package com.stratio.explorer.reader;

import com.stratio.explorer.exceptions.FileConfNotExisException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class PropertiesReaderTest {



    private Properties result ;
    private PropertiesReader reader;


    @Before
    public void setUp(){
        result = new Properties();

        reader = new PropertiesReader();

    }


    @After
    public void tearDown(){
    }


    @Test(expected = FileConfNotExisException.class)
    public void whenNotExistFile(){
        reader.readConfigFrom("not_Exist");
    }

    @Test public void whenExistFileAndContainsData(){
        result.put("cassandra.hostPort","127.0.0.1:8080");
        assertThat(reader.readConfigFrom("test_file"), is(result));
    }
}
