/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
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
package com.stratio.explorer.spark.gateways.contexts;


import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.ConnectorCreator;
import com.stratio.explorer.spark.conf.AttributteNames;
import com.stratio.explorer.spark.exception.MalformedSparkURLException;
import com.stratio.explorer.spark.functions.SparkPropertyToSparkConf;
import com.stratio.explorer.spark.lists.SparkConfComparator;
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
    private ConnectorCreator<SparkConf> creator;
    private List<String> keysToInspect ;



    @Before
    public void setUp(){
        keysToInspect = new ArrayList<>();
        keysToInspect.add(AttributteNames.CT_MASTER);
        properties = new Properties();
        creator = new ConnectorCreator<SparkConf>(new SparkConfComparator()," Porperty spark master is not filled ");
    }

    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertiestIsempty(){
        Collection<SparkConf> expected = new ArrayList<>();
        creator.buildConnections(new ArrayList<>(properties.stringPropertyNames()),new SparkPropertyToSparkConf(properties));
    }


    @Test
    public void whenPropertiesHaveOneValue(){
        String url = "mesos://";
        properties.put(AttributteNames.CT_MASTER, url);
        Collection<SparkConf> expected = Arrays.asList(new SparkConf().setMaster(url).setAppName("stratio-explorer"));
        creator.buildConnections(keysToInspect, new SparkPropertyToSparkConf(properties));
        assertTrue("Result should not be  empty list", isEquals(creator.getConnections(), expected));
    }

    private boolean isEquals(Collection<SparkConf> localConcatpoint,Collection<SparkConf> connectorConfigurer){
        List<SparkConf> firstList = new ArrayList<>(localConcatpoint);
        List<SparkConf> secondList = new ArrayList<>(connectorConfigurer);
        boolean equals = firstList.size() == secondList.size();
        for (int index=0;index<firstList.size() && equals;index++){
            equals = firstList.get(index).get(AttributteNames.CT_MASTER).equals(secondList.get(index).get(AttributteNames.CT_MASTER));
        }
        return equals;
    }


    @Test(expected =MalformedSparkURLException.class)
    public void whenURLIsNotCorrect(){
        properties.put(AttributteNames.CT_MASTER, "127.0.0.1:8080aaaaa");
        creator.buildConnections(keysToInspect,new SparkPropertyToSparkConf(properties));
    }

    @Test
    public void whenPropertiesWithSameValuesHasLoaded(){
        String anyURL = "mesos://any";
        properties.put(AttributteNames.CT_MASTER, anyURL);
        creator.buildConnections(keysToInspect,new SparkPropertyToSparkConf(properties));
        assertThat("is new connection should be true", creator.isNewConnexionLoaded(), is(true));
        creator.setNewConnection(false);
        Properties newProperties = new Properties();
        newProperties.put(AttributteNames.CT_MASTER, anyURL);
        creator.buildConnections(keysToInspect,new SparkPropertyToSparkConf(properties));
        assertThat("is new connection should be false",creator.isNewConnexionLoaded(), is(false));
    }
}
