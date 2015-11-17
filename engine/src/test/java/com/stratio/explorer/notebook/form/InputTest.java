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
package com.stratio.explorer.notebook.form;


import com.stratio.explorer.notebook.utils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public class InputTest {


    private InputExpectedValues expectedValues;
    private EntrySplitParameters splitParameters;


    @Before
    public void setUp(){
        expectedValues =  new InputExpectedValues();
        splitParameters = new EntrySplitParameters();

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

        throutghAssertsWithInput(params.get(expectedValues.name));
    }

    @Test
    public void whenCallExtractSimpleQueryParamsWithParenthesisInKey(){
        expectedValues.defaultValue = KeyValuesStore.KEY_WITH_PARENTESIS.value();
        expectedValues.name = KeyValuesStore.VALUES_WITH_PARENTHESIS[0] ;
        expectedValues.displayName = KeyValuesStore.VALUES_WITH_PARENTHESIS[1].substring(1, KeyValuesStore.VALUES_WITH_PARENTHESIS[1].length()-1);
        Map<String, Input> params =Input.extractSimpleQueryParam(ScriptTypes.DELIMITER_WITH_KEY_VALUE_WITH_PARENTHESIS_IN_KEY);
        throutghAssertsWithInput(params.get(expectedValues.name));
    }


    private void throutghAssertsWithInput (Input params){
        assertThat("Input.params should be equals to"+expectedValues.name,params.name,is(expectedValues.name));
        assertThat("Input displayName should be equals "+expectedValues.displayName,params.displayName, is(expectedValues.displayName));
        assertThat("Input type should be equals "+expectedValues.type,params.type,is(expectedValues.type));
        assertThat("Input default name should be equals "+expectedValues.defaultValue,params.defaultValue, is(expectedValues.defaultValue));


        assertThat("Input hidden value should be equals "+expectedValues.hidden,params.hidden, is(expectedValues.hidden));
        if (params.options == null)
            assertThat("Input optons should be equals to "+expectedValues.options,params.options, is(expectedValues.options));
        if (params.options != null){
            assertEquals("Input options should be equals to "+expectedValues.options.length,params.options.length,expectedValues.options.length);
            for (int index =0;index<params.options.length;index++){
                assertEquals("Input param options display name should be equals to "+expectedValues.options[index].getDisplayName(),params.options[index].getDisplayName(), expectedValues.options[index].getDisplayName());
                assertEquals("Input param options ",params.options[index].getValue(), expectedValues.options[index].getValue());
            }

        }
    }

/***************************************************************************************************************************************************************************************/
    //TODO : WHEN UPPER COVERED THEN SEPARATE IN THREE CLASS TEST
//TODO : REMOVE ALL HARDCODED NAMES
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
        througthAsserts(query, "");
    }


    @Test
    public void whenCallGetSimpleQueryWithParamsAndscriptFilled(){
        expectedValues.name = "value";


        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name,"{value = 1}");

        Object a = params.get(expectedValues.name);
        String query = Input.getSimpleQuery(params, ScriptTypes.EMPTY_OBJECT);
        througthAsserts(query,"{}");
    }



    @Test
    public void whenCallGetSimpleQueryWithParamsAndscriptFilledWithObjectWithEqualsSimbolSeparator(){
        expectedValues.name = "value";

        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name, "{"+KeyValuesStore.FIRST_KEY_VALUE.key() +"}");

        String query = Input.getSimpleQuery(params, ScriptTypes.DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_EQUALS);
        througthAsserts(query, KeyValuesStore.FIRST_KEY_VALUE.value());
    }


    @Test
    public void whenCallGetSimpleQueryWithParamsAndScriptFilledWithManyObjectSeparatebyComma(){
        expectedValues.name = "value";

        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name, "{" + KeyValuesStore.FIRST_KEY_VALUE.key() + "}");

        String query = Input.getSimpleQuery(params,ScriptTypes.DELIMITER_WITH_KEY_VALUE_OBJETC_WITH_LIT_VALUES);
        througthAsserts(query, KeyValuesStore.LITS_VALUES[0]);
    }

    @Test
    public void whenCallGetSimpleQueryWithNestedObject(){
        expectedValues.name = "value";

        Map<String,Object> params = new HashMap<>();
        params.put(expectedValues.name, "{" + KeyValuesStore.VALUE_WITH_NESTED_OBJECT.key() + "},"+"{" + KeyValuesStore.VALUE_WITH_NESTED_OBJECT.key() + "}");
        String query = Input.getSimpleQuery(params,ScriptTypes.DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_NESTED_OBJECT);
        througthAsserts(query, "a:b");
    }


    private void througthAsserts(String query,String value){
        assertThat("Query result should be equals to " + value, query, is(value));
    }


    /***************************************************************************************************************************/
    //TODO : when test ending separate in three diferen class
    @Test
    public void whenCallSplitMethodWithEmptyString(){
       String [] result = Input.split("");
        througthSplitAsserts(result, new String[]{""});
    }

    @Test
    public void whenCallSplitMethodWithAnyStringNotMactherWithRegularexpresion(){
        String [] result = Input.split("asassasasas");
        througthSplitAsserts(result, new String[]{"asassasasas"});
    }


    @Test
    public void whenCallSplitMethodWithAllParametersNotInitialize(){
        String [] result =  Input.split(splitParameters.str, splitParameters.escapeSeq,
                splitParameters.escapeChar, splitParameters.blockStart,
                splitParameters.blockEnd, splitParameters.splitters,
                splitParameters.includeSplitter);
        througthSplitAsserts(result,new String[]{});
    }


    @Test
    public void whenCallSplitMethodWithStrInitialized(){
        splitParameters.str = "aaaaaa-11111";
        String [] result =  Input.split(splitParameters.str, splitParameters.escapeSeq,
                splitParameters.escapeChar, splitParameters.blockStart,
                splitParameters.blockEnd, splitParameters.splitters,
                splitParameters.includeSplitter);
        througthSplitAsserts(result,new String[]{splitParameters.str});
    }


    @Test
    public void whenCallSplitMethodWithStringThatMatcherWithRegularExpresionAndScapeChar(){

        splitParameters.str = "aaaaaa-11111";
        splitParameters.escapeChar ='-';
        String [] result =  Input.split(splitParameters.str, splitParameters.escapeSeq,
                                        splitParameters.escapeChar, splitParameters.blockStart,
                                        splitParameters.blockEnd, splitParameters.splitters,
                                        splitParameters.includeSplitter);
        througthSplitAsserts(result,new String[]{splitParameters.str});
    }


    @Test
    public void whenCallSplitMethodWithStrScapeCharAndSplitters(){
        splitParameters.str = "aaaaaa-11111";
        splitParameters.escapeChar ='-';
        splitParameters.splitters = new String[]{"aa","s2","s3"};
        boolean includeSplitter = false;
        String [] result =  Input.split(splitParameters.str, splitParameters.escapeSeq,
                                        splitParameters.escapeChar, splitParameters.blockStart,
                                        splitParameters.blockEnd, splitParameters.splitters,
                                        splitParameters.includeSplitter);
        througthSplitAsserts(result, new String[]{"", "", "", "-11111"});
    }


    private void througthSplitAsserts(String [] result ,String [] expected){
        assertThat("Result should be equals "+expected,result,is(expected));
    }
}
