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
package com.stratio.explorer.reader;

import com.stratio.explorer.exceptions.FileConfNotExisException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class PropertiesReaderTest {



    private Properties result ;
    private PropertiesReader reader;


    @Before
    public void setUp(){
        result = new Properties();

        reader = new PropertiesReader();

    }


    @After
    public void tearDown(){
    }


    @Test(expected = FileConfNotExisException.class)
    public void whenNotExistFile(){
        reader.readConfigFrom("not_Exist");
    }

    @Test public void whenExistFileAndContainsData(){
        result.put("cassandra.host","127.0.0.1");
        result.put("cassandra.port","9042");
        assertThat(reader.readConfigFrom("test_file"), is(result));
    }
}
