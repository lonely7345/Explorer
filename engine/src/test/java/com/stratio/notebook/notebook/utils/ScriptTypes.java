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

package com.stratio.notebook.notebook.utils;


import org.apache.commons.lang.StringUtils;

public class ScriptTypes {


    public static String [] LITS_VALUES = new String[]{"one","two","three"};

    public static final String NULL_VALUE =null;
    public static final String EMPTY ="";
    public static final String NOT_VALID="SDAKIGUTDASUJBDGASKAUSTGFUYAFVCMHNGDFAS";
    public static final String HIDDEN_CHAR = "_";
    public static final String DELIMITER_CHAR="$";
    public static final String HIDDEN_CHAR_AND_DELIMITER = HIDDEN_CHAR + DELIMITER_CHAR;
    public static final String EMPTY_OBJECT=ScriptSelector.WITH_TWO_DOTS.buildNotHiddenWith();
    public static final String DELIMITER_AND_EMPTY_OBJETC=DELIMITER_CHAR +EMPTY_OBJECT;

    public static final String HIDDEN_CHAR_DELIMITER_AND_EMPTY_OBJETC = HIDDEN_CHAR+DELIMITER_CHAR+ScriptSelector.WITH_TWO_DOTS.buildNotHiddenWith();

    public static final String DELIMITER_WITH_ONE_KEY_VALUE_SEPARATE_WITH_DOTS_SEPARATOR = DELIMITER_CHAR+ScriptSelector.WITH_TWO_DOTS.buildNotHiddenWith(KeyValuesStore.FIRST_KEY_VALUE);

    public static final String DELIMITER_WITH_TWO_KEY_VALUE_OBJECT_WITH_DOTS_SEPARATOR_SEPARATE_BY_COMMAS =
              DELIMITER_CHAR+ScriptSelector.WITH_TWO_DOTS.buildNotHiddenWith(KeyValuesStore.FIRST_KEY_VALUE)+","+ScriptSelector.WITH_TWO_DOTS.buildNotHiddenWith(KeyValuesStore.SECOND_KEY_VALUE);

    public static final String DELIMITER_WITH_OBJECT_WITH_TWO_OBJECTS_WITH_EQUALS_SEPARATOR =
             DELIMITER_CHAR+ScriptSelector.WITH_EQUALS.buildNotHiddenWith(KeyValuesStore.FIRST_KEY_VALUE,KeyValuesStore.SECOND_KEY_VALUE);


    public static final String DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_EQUALS =
            DELIMITER_CHAR  + ScriptSelector.WITH_EQUALS.buildNotHiddenWith(KeyValuesStore.FIRST_KEY_VALUE);

    public static final String DELIMITER_WITH_KEY_VALUE_OBJETC_WITH_LIT_VALUES =
            DELIMITER_CHAR  + ScriptSelector.WITH_EQUALS.buildNotHiddenWith(new KeyValue(KeyValuesStore.FIRST_KEY_VALUE.key(),StringUtils.join(LITS_VALUES, ",")));


    public static final String DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_NESTED_OBJECT =
            DELIMITER_CHAR  + ScriptSelector.WITH_EQUALS.buildNotHiddenWith(new KeyValue(KeyValuesStore.FIRST_KEY_VALUE.key(),"a:b,c:d"));



}
