/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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
