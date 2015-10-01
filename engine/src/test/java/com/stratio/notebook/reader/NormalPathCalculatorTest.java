/**
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
