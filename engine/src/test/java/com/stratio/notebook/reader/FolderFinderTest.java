/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package com.stratio.notebook.reader;

import com.stratio.notebook.conf.ConstantsFolder;


import com.stratio.notebook.exceptions.FolderNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { EnvironmentPathCalculator.class })
public class FolderFinderTest {

    private FolderFinder finder;
    private String oldValueFolder = ConstantsFolder.CT_NAME_PROJECT_FOLDER;
    private String oldConf ;

    @Before
    public void setUp(){
        oldConf =  ConstantsFolder.CT_FOLDER_CONFIGURATION;
        finder = new FolderFinder();
    }

    @After
    public void tearDown(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION = oldConf;
        ConstantsFolder.CT_NAME_PROJECT_FOLDER =  oldValueFolder;
    }


    @Test
    public void parentFolderWillBeCalculated(){

        assertTrue(finder.parentProjectFolder().endsWith( ConstantsFolder.CT_FOLDER_CONFIGURATION +"/"));
    }


   @Test
    public void whenParentFolderNotExistButEnvironmentFolderExist(){
       final String expected = finder.parentProjectFolder();
        ConstantsFolder.CT_NAME_PROJECT_FOLDER = "not_exist_file";
        mockStatic(System.class);
        final String key = ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV;
        expect(System.getenv(key)).andReturn(expected).andReturn(expected);
        replayAll();
        assertThat(finder.parentProjectFolder(), is(expected));
    }

    @Test (expected = FolderNotFoundException.class)
    public void whenParentFolderNotExistAndEnvironmentFolderExist(){
        ConstantsFolder.CT_FOLDER_CONFIGURATION = "not_exist_file";
        mockStatic(System.class);
        final String key = ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV;
        final String expected = "not_exist";
        expect(System.getenv(key)).andReturn(expected);
        replayAll();
        finder.parentProjectFolder();
    }
}
