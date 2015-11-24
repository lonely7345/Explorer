/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
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

package com.stratio.explorer.cassandra.dto;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class ScriptCreateFormatterTest {



    private ScriptCreateFormatter scriptCreateFormatter;
    private String FOUR_WHITE_SPACE ="    ";

    @Before
    public void setUp(){
        scriptCreateFormatter = new ScriptCreateFormatter();
    }

    @Test
    public void whenScriptHaveReservedWordAnd(){
        String script = "CREATE TABLE DEMO.WITHFLOAT AND REPLICATION";
        String result= "CREATE TABLE DEMO.WITHFLOAT"+ System.getProperty("line.separator") +FOUR_WHITE_SPACE+"AND REPLICATION";
        assertThat("result should be Script formatted",scriptCreateFormatter.format(script),is(result));
    }


    @Test
    public void whenScriptHaveOpenParenthesis(){
        String script = "CREATE TABLE DEMO.WITHFLOAT (any)";
        String result = "CREATE TABLE DEMO.WITHFLOAT (" +System.getProperty("line.separator")
                        +FOUR_WHITE_SPACE+"any"+System.getProperty("line.separator")+")";
        assertThat("result should be Script formatted",scriptCreateFormatter.format(script),is(result));
    }


    @Test
    public void whenScriptHaveCommas(){
        String script = "CREATE TABLE DEMO.WITHFLOAT (any,other)";
        String result = "CREATE TABLE DEMO.WITHFLOAT (" +System.getProperty("line.separator")
                +FOUR_WHITE_SPACE+"any,"+System.getProperty("line.separator")+
                 FOUR_WHITE_SPACE+"other"+System.getProperty("line.separator")+")";
        assertThat("result should be Script formatted",scriptCreateFormatter.format(script),is(result));
    }
}
