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

package com.stratio.notebook.writer;


import com.stratio.notebook.Commons;
import com.stratio.notebook.reader.PropertiesReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertiesFileUpdaterTest {


    private Properties result;
    private Commons commons;
    private static final String CT_FILE="changer";
    private PropertiesFileUpdater updater;


    @Before public void setUp(){
        result = new Properties();
        commons = new Commons();
        commons.initialize();
        updater = new PropertiesFileUpdater();
    }


    @After public void tearDown(){
        commons.tearDown();
        updater.updateFileWithProperties(CT_FILE,"prop1=prop8");
    }

    @Test public void whenFileContainsPropertyThenActualizeValue() throws IOException {
        result.put("prop1","prop8");
        updater.updateFileWithProperties(CT_FILE,"prop1=prop8");
        assertThat(new PropertiesReader().readConfigFrom(CT_FILE), is(result));
    }
}
