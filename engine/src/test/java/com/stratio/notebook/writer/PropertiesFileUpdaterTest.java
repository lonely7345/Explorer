package com.stratio.notebook.writer;


import com.stratio.notebook.reader.PathFileCalculator;
import com.stratio.notebook.reader.PropertiesReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertiesFileUpdaterTest {


    private Properties result;
    private static final String CT_FILE="changer";
    private PropertiesFileUpdater updater;


    @Before public void setUp(){
        result = new Properties();
        updater = new PropertiesFileUpdater();
    }


    @After public void tearDown(){
        new File(new PathFileCalculator().getPath(CT_FILE)).delete();
    }

    @Test public void whenFileContainsPropertyThenActualizeValue() throws IOException {
        result.put("prop1","prop8");
        updater.updateFileWithProperties(CT_FILE,"prop1=prop8");
        assertThat(new PropertiesReader().readConfigFrom(CT_FILE), is(result));
    }
}
