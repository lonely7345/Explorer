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
