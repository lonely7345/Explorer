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
import com.stratio.notebook.notebook.utils.KeyValue;
import com.stratio.notebook.notebook.utils.ParamsBuilder;
import com.stratio.notebook.notebook.utils.ScriptObjectBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


//TODO : WHEN TEST PASSED THEN REFACTOR
public class InputTest {


    private final String CT_HIDDEN_CHARACTER = "_";
    private final String CT_INIT_JSON_OBJECT="$";

    private InputExpectedValues expectedValues;
    private ScriptObjectBuilder scriptObjectBuilder;
    private KeyValue keyValueOne ;
    private KeyValue keyValueTwo ;


    @Before
    public void setUp(){
        expectedValues =  new InputExpectedValues();
        scriptObjectBuilder = new ScriptObjectBuilder();
        keyValueOne = new KeyValue("firstAnyKey","firstAnyValue");
        keyValueTwo = new KeyValue("secondAnyKey","secondAnyValue");
    }

    @After
    public void tearDown(){
        expectedValues =null;
        scriptObjectBuilder = null;
        keyValueOne = null;
        keyValueTwo = null;
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
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith());
        assertThat(params.size(), is(1));
        throutghAssertsWithInput(params.get(""));
    }


    @Test
    public void whenCallExractSimpleQueryParamWithHiddenCharDelimiterAndEmptyObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(CT_HIDDEN_CHARACTER+CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith());
        expectedValues.hidden = true;
        assertThat(params.size(), is(1));
        throutghAssertsWithInput(params.get(""));
    }

    @Test
    public void whenCallExtractSimpleQueryParamsWithDelimiterAndFilledWithJsonObject(){

        String script = CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith(keyValueOne);
        Map<String, Input> params =Input.extractSimpleQueryParam(script);
        assertThat(params.size(), is(1));
        expectedValues.type = keyValueOne.key();
        expectedValues.name = keyValueOne.value();
        throutghAssertsWithInput(params.get(expectedValues.name));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithDelimiterAndTwoSimpleJsonObject(){

        String script = CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith(keyValueOne)+","+scriptObjectBuilder.buildNotHiddenWith(keyValueTwo);
        Map<String, Input> params =Input.extractSimpleQueryParam(script);
        assertThat(params.size(), is(1));
        expectedValues.type = keyValueOne.key();
        expectedValues.name = keyValueOne.value();
        throutghAssertsWithInput(params.get(expectedValues.name));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithComplexJsonObject(){
        String script = CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith(keyValueOne,keyValueTwo);
        Map<String, Input> params =Input.extractSimpleQueryParam(script);
        assertThat(params.size(), is(1));
        expectedValues.type = keyValueOne.key();
        expectedValues.name = keyValueOne.value()+","+keyValueTwo.key()+":"+keyValueTwo.value();
        throutghAssertsWithInput(params.get(expectedValues.name ));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithComplexObjectSeparateByEqualsSymbol(){
        scriptObjectBuilder.changeSimbolSeparatorKeyValue("=");;
        String script = CT_INIT_JSON_OBJECT+scriptObjectBuilder.buildNotHiddenWith(keyValueOne,keyValueTwo);
        Map<String, Input> params =Input.extractSimpleQueryParam(script);
        expectedValues.defaultValue = keyValueOne.value();
        expectedValues.name = keyValueOne.key() ;

        expectedValues.options =  new Input.ParamOption[] {new Input.ParamOption(keyValueTwo.key()+"="+keyValueTwo.value(),null)};
        throutghAssertsWithInput(params.get(expectedValues.name ));
    }


    @Test (expected = NullPointerException.class)
    public void whenCallgetSimpleQueryWithNullParams(){
        String script ="anyScrips";
        String query =Input.getSimpleQuery(null,script);
    }


    @Test (expected = NullPointerException.class)
    public void whenCallGetSimpleQueryWithNullScrips(){
        Map<String,Object> params = ParamsBuilder.buildParamsByExpectedInput(expectedValues);
        Input.getSimpleQuery(params,null);
    }


    @Test
    public void whenCallGetSimpleQuerywithEmptyScripts(){
        Map<String,Object> params = ParamsBuilder.buildParamsByExpectedInput(expectedValues);
        String query = Input.getSimpleQuery(params, "");
        assertThat(query,is(""));
    }


    @Test
    public void whenCallGetSimpleQueryWithParamsAndscriptFilled(){

    }




    private void throutghAssertsWithInput (Input params){
        assertThat(params.name,is(expectedValues.name));
        assertThat(params.displayName,is(expectedValues.displayName));
        assertThat(params.type,is(expectedValues.type));
        assertThat(params.defaultValue,is(expectedValues.defaultValue));


        assertThat(params.hidden, is(expectedValues.hidden));
        if (params.options == null)
            assertThat(params.options, is(expectedValues.options));
        if (params.options != null){
            assertEquals(params.options.length,expectedValues.options.length);
            for (int index =0;index<params.options.length;index++){
                assertEquals(params.options[index].getDisplayName(), expectedValues.options[index].getDisplayName());
                assertEquals(params.options[index].getValue(), expectedValues.options[index].getValue());
            }

        }
    }
}
