package com.stratio.explorer.notebook.utils;


import org.apache.commons.lang.StringUtils;

public class KeyValuesStore {

    public static String [] VALUES_WITH_PARENTHESIS = new String [] {"first","(second)"};
    public static String [] LITS_VALUES = new String[]{"one","two"};


    public static final KeyValue FIRST_KEY_VALUE = new KeyValue("firstAnyKey","firstAnyValue");
    public static final KeyValue SECOND_KEY_VALUE = new KeyValue("secondAnyKey","secondAnyValue");
    public static final KeyValue KEY_WITH_PARENTESIS = new KeyValue(StringUtils.join(VALUES_WITH_PARENTHESIS, ""),"a");
    public static final KeyValue VALUE_WITH_NESTED_OBJECT = new KeyValue("ANYkEY","a:b,c:d");
    public static final KeyValue VALUE_WITH_LIST_VALUES  =new KeyValue(KeyValuesStore.FIRST_KEY_VALUE.key(),StringUtils.join(LITS_VALUES, ","));
}
