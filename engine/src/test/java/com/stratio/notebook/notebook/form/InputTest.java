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

package com.stratio.notebook.notebook.form;


import com.stratio.notebook.notebook.utils.InputExpectedValues;
import com.stratio.notebook.notebook.utils.ScriptObjectBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;



public class InputTest {


    private final String CT_HIDDEN_CHARACTER = "_";
    private final String CT_INIT_JSON_OBJECT="$";
    private final String CT_EMPTY_JSON = "{}";
    private final String CT_KEY_A = "a";
    private final String CT_VALUE_ONE ="1";
    private final String CT_KEY_B = "b";
    private final String CT_VALUE_TWO ="2";
    private final String CT_SIMPLE_JSON_OBJECT = "{"+CT_KEY_A+":"+CT_VALUE_ONE+"}";
    private final String CT_COMPLEX_JSON_OBJECT = "{"+CT_KEY_A+":"+CT_VALUE_ONE+","+CT_KEY_B+":"+CT_VALUE_TWO+"}";
    private InputExpectedValues expectedValues;
    private ScriptObjectBuilder scriptObjectBuilder;


    @Before
    public void setUp(){
        expectedValues =  new InputExpectedValues();
        scriptObjectBuilder = new ScriptObjectBuilder();
    }

    @After
    public void tearDown(){
        expectedValues =null;
        scriptObjectBuilder = null;
    }

    @Test
    public void whenCallExtractSimpleQueryParamWithEmptyString(){
        Map<String, Input> params =Input.extractSimpleQueryParam("");
        assertTrue(params.isEmpty());
    }


    @Test
    public void whenCallExtractSimpleQueryParamWithNotValidString(){
        Map<String, Input> params =Input.extractSimpleQueryParam("not_valid_string");
        assertTrue(params.isEmpty());
    }


    @Test
    public void whenCallExtractSimpleQueryParamWithNullValue(){
        Map<String, Input> params =Input.extractSimpleQueryParam(null);
        assertTrue(params.isEmpty());
    }

    @Test
    public void whenCallExtractSimpleQueryParamWithHiddenCharacter(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_HIDDEN_CHARACTER);
        assertTrue(params.isEmpty());
    }

    @Test
    public void whenCallExtractSimpleQueryParamWithHiddenCharacterAndDelimiter(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_HIDDEN_CHARACTER+CT_INIT_JSON_OBJECT);
        assertTrue(params.isEmpty());
    }

    @Test
    public void whenCallExractSimpleQueryParamWithOnlyJsonObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam("{}");
        assertTrue(params.isEmpty());
    }


    @Test
    public void whenCallExtractsimpleQueryParamWithDelimiterAndEmptyObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_INIT_JSON_OBJECT+"{}");
        assertThat(params.size(), is(1));
        throutghAssertsWithInput(params.get(""));
    }


    @Test
    public void whenCallExractSimpleQueryParamWithHiddenCharDelimiterAndEmptyObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_HIDDEN_CHARACTER+CT_INIT_JSON_OBJECT+"{}");
        expectedValues.hidden = true;
        assertThat(params.size(), is(1));
        throutghAssertsWithInput(params.get(""));
    }

    @Test
    public void whenCallExtractSimpleQueryParamsWithDelimiterAndFilledWithJsonObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith(CT_KEY_A,CT_VALUE_ONE));
        assertThat(params.size(), is(1));
        expectedValues.type = CT_KEY_A;
        expectedValues.name = CT_VALUE_ONE;
        throutghAssertsWithInput(params.get(expectedValues.name));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithDelimiterAndTwoSimpleJsonObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_INIT_JSON_OBJECT+CT_SIMPLE_JSON_OBJECT+",{anyKey : anyValue}");
        assertThat(params.size(), is(1));
        expectedValues.type = CT_KEY_A;
        expectedValues.name = CT_VALUE_ONE;
        throutghAssertsWithInput(params.get(expectedValues.name));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithComplexJsonObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_INIT_JSON_OBJECT+CT_COMPLEX_JSON_OBJECT);
        assertThat(params.size(), is(1));
        expectedValues.type = CT_KEY_A;
        expectedValues.name = CT_VALUE_ONE+","+CT_KEY_B+":"+CT_VALUE_TWO;
        throutghAssertsWithInput(params.get(expectedValues.name ));
    }



    //TODO : SURELY THIS METHOD WILL BE MOVED INTO OTHER CLASS
    private void throutghAssertsWithInput (Input params){
        assertThat(params.name,is(expectedValues.name));
        assertThat(params.displayName,is(expectedValues.displayName));
        assertThat(params.type,is(expectedValues.type));
        assertThat(params.defaultValue,is(expectedValues.defaultValue));

        assertThat(params.options, is(expectedValues.options));
        assertThat(params.hidden, is(expectedValues.hidden));
    }



}
