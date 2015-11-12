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
package com.stratio.explorer.cassandra.gateways;


import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;



/**
 * Created by afidalgo on 15/10/15.
 */
public class CassandraSessionTest {

    private Connector session;
    private Properties properties;

    @Before
    public void setUp(){
        session = new CassandraSession();
        properties = new Properties();
    }


    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertiesNotExist(){

         session.loadConfiguration(properties);
    }

    @Test (expected = NotPropertyFoundException.class)
    public void whenPortIsEmpty(){
        properties.put(StringConstants.HOST, "127.0.1.1");
        session.loadConfiguration(properties);
    }


    @Test (expected = NotPropertyFoundException.class)
    public void whenHostIsEmpty(){
        properties.put(StringConstants.PORT, "123");
        session.loadConfiguration(properties);
    }
}
