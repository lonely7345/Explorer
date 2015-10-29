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


import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.cassandra.exceptions.NotValidPortException;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.*;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class CasandraConnectorCreatorTest {

    private Properties properties;
    private CasandraConnectorCreator reader;

    @Before
    public void setUp(){
        properties = new Properties();
        reader = new CasandraConnectorCreator();
    }

    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertiestIsempty(){
        Collection<InetSocketAddress> expected = new ArrayList<>();
        reader.buildConnections(properties);
    }


    @Test
    public void whenPropertiesHaveOneValue(){
        properties.put("cassandra.concat_point", "127.0.0.1:8080");
        Collection<InetSocketAddress> expected = Arrays.asList(new InetSocketAddress("127.0.0.1",8080));
        reader.buildConnections(properties);
        assertThat("Result should be empty list", reader.getConnections(), is(expected));
    }


    @Test(expected =NotValidPortException.class)
    public void whenPortIsNotNumber(){
        properties.put("cassandra.concat_point", "127.0.0.1:8080aaaaa");
        reader.buildConnections(properties);
    }

    @Test
    public void whenPropertiesWithSameValuesHasLoaded(){
        properties.put("cassandra.concat_point", "127.0.0.1:8080");
        reader.buildConnections(properties);
        assertThat(reader.isNewConnexionLoaded(), is(true));
        reader.setNewConnection(false);
        Properties newProperties = new Properties();
        newProperties.put("cassandra.concat_point", "127.0.0.1:8080");
        reader.buildConnections(newProperties);
        assertThat(reader.isNewConnexionLoaded(), is(false));
    }



}
