package com.stratio.explorer.converters;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Properties;

public class PropertiesToStringConverterTest {

    private Properties properties ;
    private PropertiesToStringConverter converter ;
    private String CT_KEY_VALUE_1="keyValue1";
    private String CT_KEY_VALUE_2="keyValue2";
    private String CT_LINE_SEPARATOR ="0909090";

    @Before public void setUp(){
        properties = new Properties();
        converter = new PropertiesToStringConverter(CT_LINE_SEPARATOR);
    }

    @Test public void whenPropertiesNotHaveValuesResultShouldBeEmptyString(){
        assertThat(converter.transform(properties),is(""));
    }

    @Test public void whenPropertiesHaveValuesThenReturnStringWithLines(){
        properties.put(CT_KEY_VALUE_1,CT_KEY_VALUE_1);
        properties.put(CT_KEY_VALUE_2,CT_KEY_VALUE_2);
        String result = CT_KEY_VALUE_1 +"=" + CT_KEY_VALUE_1 + CT_LINE_SEPARATOR + CT_KEY_VALUE_2 +"=" + CT_KEY_VALUE_2 ;
        assertThat(converter.transform(properties),is(result));
    }
}
