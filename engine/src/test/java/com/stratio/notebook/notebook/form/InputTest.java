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


import com.stratio.notebook.notebook.utils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;



public class InputTest {


    private InputExpectedValues expectedValues;


    @Before
    public void setUp(){
        expectedValues =  new InputExpectedValues();

    }

    @After
    public void tearDown(){
        expectedValues =null;
    }

    @Test
    public void whenCallExtractSimpleQueryParamWithEmptyString(){
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.EMPTY);
        assertTrue(params.isEmpty());
    }


    @Test
    public void whenCallExtractSimpleQueryParamWithNotValidString(){
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.NOT_VALID);
        assertTrue(params.isEmpty());
    }


    @Test
    public void whenCallExtractSimpleQueryParamWithNullValue(){
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.NULL_VALUE);
        assertTrue(params.isEmpty());
    }

    @Test
    public void whenCallExtractSimpleQueryParamWithHiddenCharacter(){
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.HIDDEN_CHAR);
        assertTrue(params.isEmpty());
    }

    @Test
    public void whenCallExtractSimpleQueryParamWithHiddenCharacterAndDelimiter(){
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.HIDDEN_CHAR_AND_DELIMITER);
        assertTrue(params.isEmpty());
    }

    @Test
    public void whenCallExractSimpleQueryParamWithOnlyJsonObject(){
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.EMPTY_OBJECT);
        assertTrue(params.isEmpty());
    }


    @Test
    public void whenCallExtractsimpleQueryParamWithDelimiterAndEmptyObject(){

        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.DELIMITER_AND_EMPTY_OBJETC);

        assertThat(params.size(), is(1));
        throutghAssertsWithInput(params.get(""));
    }


    @Test
    public void whenCallExractSimpleQueryParamWithHiddenCharDelimiterAndEmptyObject(){
        expectedValues.hidden = true;


        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.HIDDEN_CHAR_DELIMITER_AND_EMPTY_OBJETC);

        assertThat(params.size(), is(1));
        throutghAssertsWithInput(params.get(""));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithDelimiterAndFilledWithJsonObject(){
        expectedValues.type = KeyValuesStore.FIRST_KEY_VALUE.key();
        expectedValues.name = KeyValuesStore.FIRST_KEY_VALUE.value();

        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.DELIMITER_WITH_ONE_KEY_VALUE_SEPARATE_WITH_DOTS_SEPARATOR);
        assertThat(params.size(), is(1));

        throutghAssertsWithInput(params.get(expectedValues.name));
    }


    @Test
    public void whenCallExtractSimpleQueryParamsWithDelimiterAndTwoSimpleJsonObject(){
        expectedValues.type = KeyValuesStore.FIRST_KEY_VALUE.key();
        expectedValues.name = KeyValuesStore.FIRST_KEY_VALUE.value();

        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.DELIMITER_WITH_TWO_KEY_VALUE_OBJECT_WITH_DOTS_SEPARATOR_SEPARATE_BY_COMMAS);
        assertThat(params.size(), is(1));

        throutghAssertsWithInput(params.get(expectedValues.name));
    }



    @Test
    public void whenCallExtractSimpleQueryParamsWithComplexObjectSeparateByEqualsSymbol(){

        expectedValues.defaultValue = KeyValuesStore.FIRST_KEY_VALUE.value();
        expectedValues.name = KeyValuesStore.FIRST_KEY_VALUE.key() ;
        expectedValues.options =  new Input.ParamOption[] {new Input.ParamOption(KeyValuesStore.SECOND_KEY_VALUE.toStringSeparateBysimbol("="),null)};

        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.DELIMITER_WITH_OBJECT_WITH_TWO_OBJECTS_WITH_EQUALS_SEPARATOR);

        throutghAssertsWithInput(params.get(expectedValues.name ));
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


    //TODO : WHEN UPPER COVERED THEN SEPARATE IN THREE CLASS TEST
    @Test (expected = NullPointerException.class)
    public void whenCallgetSimpleQueryWithNullParams(){
        String script ="anyScrips";
        String query =Input.getSimpleQuery(null, script);
    }


    @Test (expected = NullPointerException.class)
    public void whenCallGetSimpleQueryWithNullScrips(){
        Map<String,Object> params = ParamsBuilder.buildParamsByExpectedInput(expectedValues);
        Input.getSimpleQuery(params, ScriptTypes.NULL_VALUE);
    }


    @Test
    public void whenCallGetSimpleQuerywithEmptyScripts(){
        Map<String,Object> params = ParamsBuilder.buildParamsByExpectedInput(expectedValues);
        String query = Input.getSimpleQuery(params, ScriptTypes.EMPTY);
        assertThat(query, is(""));
    }


    @Test
    public void whenCallGetSimpleQueryWithParamsAndscriptFilled(){
        expectedValues.name = "value";


        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name,"{value = 1}");

        Object a = params.get(expectedValues.name);
        String query = Input.getSimpleQuery(params, ScriptTypes.EMPTY_OBJECT);
        assertThat(query,is("{}"));
    }



    @Test
    public void whenCallGetSimpleQueryWithParamsAndscriptFilledWithObjectWithEqualsSimbolSeparator(){
        expectedValues.name = "value";

        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name, "{"+KeyValuesStore.FIRST_KEY_VALUE.key() +"}");

        String query = Input.getSimpleQuery(params,ScriptTypes.DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_EQUALS);
        assertThat(query,is(KeyValuesStore.FIRST_KEY_VALUE.value()));
    }


    @Test
    public void whenCallGetSimpleQueryWithParamsAndScriptFilledWithManyObjectSeparatebyComma(){
        expectedValues.name = "value";

        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name, "{" + KeyValuesStore.FIRST_KEY_VALUE.key() + "}");

        String query = Input.getSimpleQuery(params,ScriptTypes.DELIMITER_WITH_KEY_VALUE_OBJETC_WITH_LIT_VALUES);
        assertThat(query, is(ScriptTypes.LITS_VALUES[0]));
    }

    @Test
    public void whenCallGetSimpleQueryWithNestedObject(){
        expectedValues.name = "value";

        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name, "{" + KeyValuesStore.FIRST_KEY_VALUE.key() + "},"+"{" + KeyValuesStore.FIRST_KEY_VALUE.key() + "}");
        String query = Input.getSimpleQuery(params,ScriptTypes.DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_NESTED_OBJECT);
        assertThat(query, is("a:b"));
    }

}
