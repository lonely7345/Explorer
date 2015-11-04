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

package com.stratio.explorer.spark.gateways.contexts;


import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.spark.conf.AttributteNames;
import com.stratio.explorer.spark.doubles.SparkContextMock;


import com.stratio.explorer.spark.exception.PropertyHiveContextValueException;
import com.stratio.explorer.spark.exception.PropertyHiveNotFilledException;
import com.stratio.explorer.spark.gateways.contexts.ExplorerSparkSQLContext;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

//TODO : CHANGE EMBEDED SPARK CONTEXT BY MOCKER
public class ExplorerSQLContextTest {



    private final String CT_MEMORY ="spark.driver.memory";
    private final String CT_CORES ="spark.cores.max";

    private final String CT_HIVE_CONTEXT ="spark.hiveContext";

    private Connector<SparkContext> sparkContext;
    private ExplorerSparkSQLContext sqlContext;
    private Properties properties;

    @Before
    public void setUp(){
        sparkContext = new SparkContextMock();
        sparkContext.loadConfiguration(null);
        sqlContext = new ExplorerSparkSQLContext(sparkContext);
        properties = new Properties();
        properties.put(AttributteNames.CT_DRIVER_MEMORY,"512M");
        properties.put(AttributteNames.CT_EXECUTOR_MEMORY,"512M");
        properties.put(AttributteNames.CT_CORES,"4");
    }


    @After
    public void tearDown(){
        sparkContext.getConnector().stop();
    }

    @Test
    public void whenHiveContextIsSelectedInConfiguration(){

       Properties properties = new Properties();
       properties.put(AttributteNames.CT_HIVE_CONTEXT,"YES");
       sqlContext.loadConfiguration(properties);
       assertThat("When exist HiveContext then create instanceof HiveContext", sqlContext.getConnector(), instanceOf(HiveContext.class));
    }

    @Test
    public void whenHiveContextIsNotSelectedInConfiguration(){

        properties.put(AttributteNames.CT_HIVE_CONTEXT,"NO");
        sqlContext.loadConfiguration(properties);
        assertThat("When exist HiveContext then create instanceof SQLContext", sqlContext.getConnector(), instanceOf(SQLContext.class));
    }


    @Test(expected = PropertyHiveNotFilledException.class)
    public void wheHiveContextPropertyNotExist(){
        sqlContext.loadConfiguration(properties);
        sqlContext.getConnector();
    }


    @Test (expected = PropertyHiveContextValueException.class)
    public void whenHiveContextPropertyIsNotCorrect(){
        properties.put(CT_HIVE_CONTEXT,"OTHER");
        sqlContext.getConnector();
    }
}
