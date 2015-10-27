package com.stratio.explorer.notebook.utils;


//TODO : when finish test move KeyValues to KeyValuesStore
public class ScriptTypes {




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
            DELIMITER_CHAR  + ScriptSelector.WITH_EQUALS.buildNotHiddenWith(KeyValuesStore.VALUE_WITH_LIST_VALUES);


    public static final String DELIMITER_WITH_KEY_VALUE_OBJECT_WITH_NESTED_OBJECT =
            DELIMITER_CHAR  + ScriptSelector.WITH_EQUALS.buildNotHiddenWith(KeyValuesStore.VALUE_WITH_NESTED_OBJECT);

    public static final String DELIMITER_WITH_KEY_VALUE_WITH_PARENTHESIS_IN_KEY =
            DELIMITER_CHAR  + ScriptSelector.WITH_EQUALS.buildNotHiddenWith(KeyValuesStore.KEY_WITH_PARENTESIS);


}
