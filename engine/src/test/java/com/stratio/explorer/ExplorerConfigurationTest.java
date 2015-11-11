/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
