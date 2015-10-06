package com.stratio.notebook.reader;

import com.stratio.notebook.conf.ConstantsFolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class NormalPathCalculatorTest {

    private String oldValueFolderConfiguration;

    private NormalPathCalculator calculator;


    @Before
    public void setUp(){
        oldValueFolderConfiguration = ConstantsFolder.CT_FOLDER_CONFIGURATION;



    }


    @After
    public void shutDown(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION   = oldValueFolderConfiguration;
    }


    @Test
    public void whenFolderInTheNextLevelOfTree(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION = "/src";
        calculator = new NormalPathCalculator(ConstantsFolder.CT_FOLDER_CONFIGURATION);
        String folder =calculator.calculatePath().toString();
        assertTrue(folder.endsWith("/engine/src"));
    }

    @Test
    public void whenFolderNotInTheNextLevelOfTree(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION = "/srcaaaa";
        calculator = new NormalPathCalculator(ConstantsFolder.CT_FOLDER_CONFIGURATION);
        String folder =calculator.calculatePath().toString();
        assertTrue(folder.equals(ConstantsFolder.CT_NOT_EXIST_FOLDER));
    }

}
