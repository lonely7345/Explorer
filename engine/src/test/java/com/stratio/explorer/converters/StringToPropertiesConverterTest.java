package com.stratio.explorer.converters;

import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class StringToPropertiesConverterTest {

    private StringToPropertiesConverter converter;
    private Properties prop;

    private String CT_KEY_VALUE_1 ="kayValue1";

    @Before public void setUp(){
        converter = new StringToPropertiesConverter();
        prop = new Properties();
    }


    @Test public void whenStringIsEmptyPropertiesShouldBeEmpty(){

        String message="";
        assertThat(converter.transform(message),is(prop));
    }


    @Test public void whenStringContainsOneProperty(){
        String message=CT_KEY_VALUE_1+"="+CT_KEY_VALUE_1;
        prop.put(CT_KEY_VALUE_1,CT_KEY_VALUE_1);
        assertThat(converter.transform(message), is(prop));
    }
}
