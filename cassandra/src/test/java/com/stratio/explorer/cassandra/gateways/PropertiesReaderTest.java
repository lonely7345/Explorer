/*
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

package com.stratio.explorer.cassandra.gateways;


import com.stratio.explorer.cassandra.exceptions.NotValidPortException;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.*;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class PropertiesReaderTest {

    private Properties properties;
    private PropertiesReader reader;

    @Before
    public void setUp(){
        properties = new Properties();
        reader = new PropertiesReader();
    }

    @Test
    public void whenPropertiestIsempty(){
        Collection<InetSocketAddress> expected = new ArrayList<>();
        assertThat("Result should be empty list",reader.getListSocketAddres(properties),is(expected));
    }


    @Test
    public void whenPropertiesHaveOneValue(){
        properties.put("cassandra.concat_point", "127.0.0.1:8080");
        Collection<InetSocketAddress> expected = Arrays.asList(new InetSocketAddress("127.0.0.1",8080));
        assertThat("Result should be empty list",reader.getListSocketAddres(properties),is(expected));
    }

    @Test
    public void other(){
        properties.put("cassandra.concat_point2", "127.0.0.2:8081");
        properties.put("cassandra.concat_point1", "127.0.0.1:8080");

        Collection<InetSocketAddress> expected = Arrays.asList(new InetSocketAddress("127.0.0.1",8080),new InetSocketAddress("127.0.0.2", 8081));

        boolean eq = expected.containsAll(reader.getListSocketAddres(properties));
        int k=0;
    }


    @Test(expected =NotValidPortException.class)
    public void whenPortIsNotNumber(){
        properties.put("cassandra.concat_point", "127.0.0.1:8080aaaaa");
        reader.getListSocketAddres(properties);
    }

}
