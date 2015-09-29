/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.notebook.reader;

import com.stratio.notebook.conf.ConstantsFolder;
import com.stratio.notebook.exceptions.FolderNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//TODO : if class contains STRATIO-NOTEBOOK FOLLOW NORMAL FLOW
//TODO : if class not contains STRATIO-NOTEBOOK
//TODO : PATH TO SEAACH /etc/sds/notebook
public class FolderFinderTest {


    private FolderFinder finder;
    private String oldValueFolder = ConstantsFolder.CT_NAME_PROJECT_FOLDER;


    @Before
    public void setUp(){
        finder = new FolderFinder();
    }

    @After
    public void tearDown(){
        ConstantsFolder.CT_NAME_PROJECT_FOLDER =  oldValueFolder;
    }


    @Test
    public void parentFolderWillBeCalculated(){
        assertTrue(finder.parentProjectFolder().endsWith("/" + ConstantsFolder.CT_NAME_PROJECT_FOLDER + "/"));
    }


    @Test(expected = FolderNotFoundException.class)
    public void whenFolderNotExistThenPathShouldBeSlash(){
        ConstantsFolder.CT_NAME_PROJECT_FOLDER = "not_exist_file";
        finder.parentProjectFolder();
    }





}
