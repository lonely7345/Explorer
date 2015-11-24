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
package com.stratio.explorer.spark.functions;

import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import org.apache.spark.SparkConf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


public class SparkPropertyToSparkConfTest {


    private Properties properties;
    private SparkPropertyToSparkConf transform;
    private final String  CT_SPARK_MASTER ="spark.master";



    @Before
    public void setUp(){
        properties = new Properties();
        transform  = new SparkPropertyToSparkConf(properties);
    }


    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertyNotExis(){
        transform.transform(CT_SPARK_MASTER);
    }

    @Test(expected = MasterPropertyNotFilledException.class)
    public void whenPropertyIsNotfilled(){
        String anyUrl = "";
        properties.put(CT_SPARK_MASTER, anyUrl);
        transform.transform(CT_SPARK_MASTER);
    }

    @Test
    public void whenPassAllChecked(){
        String anyUrl = "mesos://any";
        properties.put(CT_SPARK_MASTER, anyUrl);
        assertThat("Result should be be SparkConf object",transform.transform(CT_SPARK_MASTER).get(CT_SPARK_MASTER), is(anyUrl));
    }
}
