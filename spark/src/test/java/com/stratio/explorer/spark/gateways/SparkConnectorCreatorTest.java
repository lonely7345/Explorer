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

package com.stratio.explorer.spark.gateways;


import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.spark.exception.MalformedSparkURLException;
import org.apache.spark.SparkConf;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SparkConnectorCreatorTest {

    private Properties properties;
    private SparkConnectorCreator creator;
    private final String CT_MASTER ="spark.master";


    @Before
    public void setUp(){
        properties = new Properties();
        creator = new SparkConnectorCreator();
    }

    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertiestIsempty(){
        Collection<SparkConf> expected = new ArrayList<>();
        creator.buildConnections(properties);
    }


    @Test
    public void whenPropertiesHaveOneValue(){
        String url = "mesos://";
        properties.put(CT_MASTER, url);
        Collection<SparkConf> expected = Arrays.asList(new SparkConf().setMaster(url).setAppName("stratio-explorer"));
        creator.buildConnections(properties);
        assertTrue("Result should not be  empty list", isEquals(creator.getConnections(), expected));
    }

    private boolean isEquals(Collection<SparkConf> localConcatpoint,Collection<SparkConf> connectorConfigurer){
        List<SparkConf> firstList = new ArrayList<>(localConcatpoint);
        List<SparkConf> secondList = new ArrayList<>(connectorConfigurer);
        boolean equals = firstList.size() == secondList.size();
        for (int index=0;index<firstList.size() && equals;index++){
            equals = firstList.get(index).get(CT_MASTER).equals(secondList.get(index).get(CT_MASTER));
        }
        return equals;
    }


    @Test(expected =MalformedSparkURLException.class)
    public void whenURLIsNotCorrect(){
        properties.put(CT_MASTER, "127.0.0.1:8080aaaaa");
        creator.buildConnections(properties);
    }

    @Test
    public void whenPropertiesWithSameValuesHasLoaded(){
        String anyURL = "mesos://any";
        properties.put("spark.master", anyURL);
        creator.buildConnections(properties);
        assertThat("is new connection should be true", creator.isNewConnexionLoaded(), is(true));
        creator.setNewConnection(false);
        Properties newProperties = new Properties();
        newProperties.put("spark.master", anyURL);
        creator.buildConnections(newProperties);
        assertThat("is new connection should be false",creator.isNewConnexionLoaded(), is(false));
    }
}
