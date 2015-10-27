package com.stratio.explorer;


import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.conf.ExplorerConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import  static org.junit.Assert.fail;

public class ExplorerConfigurationTest {

    private String originalFolder;

    @Before
    public void setUp(){
        originalFolder = ConstantsFolder.CT_NAME_FILE_INTERPRETERS_CONFIGURE;
    }


    @After
    public void tearDown(){
        ConstantsFolder.CT_NAME_FILE_INTERPRETERS_CONFIGURE =  originalFolder;
    }



    @Test
    public void whenUrlExistReturnNewInstanceExplorerConfiguration(){
        try {
            ExplorerConfiguration configuration = ExplorerConfiguration.create(ConstantsFolder.CT_NAME_FILE_INTERPRETERS_CONFIGURE);
            configuration.getExplorerDir();
        }catch (RuntimeException e){
            fail();
        }
    }


    @Test (expected =  RuntimeException.class)
    public void whenURLNotExistThrowRuntimeException(){
        String fileNotExist ="fileNotExist";
        ExplorerConfiguration configuration = ExplorerConfiguration.create(fileNotExist);
    }

    @Test(expected =  RuntimeException.class)
    public void whenFolderNotexist(){
        ConstantsFolder.CT_NAME_FILE_INTERPRETERS_CONFIGURE = "badFolder";
        ExplorerConfiguration configuration = ExplorerConfiguration.create(ConstantsFolder.CT_NAME_FILE_INTERPRETERS_CONFIGURE);
    }
}
