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
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import com.stratio.explorer.spark.exception.SparkEndPointException;
import com.stratio.explorer.spark.lists.SparkConfComparator;
import org.apache.spark.SparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class ExplorerSparkContextTest {

    private Properties properties;
    private ExplorerSparkContext sparkContex;
    private final String CT_SPARK_MASTER ="spark.master";
    private final String CT_ANY_MESOS = "mesos://HOST:5050";
    private final String CT_LOCAL ="local[*]";


    @Before
    public void setUp(){
        properties = new Properties();
        sparkContex = new ExplorerSparkContext(new SparkConfComparator());
    }


    @After
    public void tearDown(){
       try {
           SparkContext context = sparkContex.getConnector();
           if (context != null) {
               context.stop();
           }
       }catch (SparkEndPointException e){
          // left empty deliverely
       }
    }

    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertysparkMasterNotExist(){
        sparkContex.loadConfiguration(properties);
    }


    @Test(expected = MasterPropertyNotFilledException.class)
    public void whenSparkContextExistbutNotFilled(){
        properties.put(CT_SPARK_MASTER, "");
        sparkContex.loadConfiguration(properties);
    }


    @Test(expected = SparkEndPointException.class)
    public void whenSparkContextisNotexist(){
        properties.put(CT_SPARK_MASTER, "mesos://HOST:5050");
        sparkContex.loadConfiguration(properties);
        sparkContex.getConnector();
    }


    @Test
    public void whenLoadSamePropertiesLoadTwoTimes(){
        try {
            properties.put(CT_SPARK_MASTER, CT_LOCAL);
            sparkContex.loadConfiguration(properties);
            sparkContex.getConnector();
            sparkContex.getConnector();
            assertTrue("When load two times same configuration not try new connection ", true);

        }catch (Throwable e){
            fail("When load two times same configuration not try new connection ");
        }
    }
}
