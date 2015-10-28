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

package com.stratio.explorer.spark.gateways;

import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.lists.FunctionalList;
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import com.stratio.explorer.spark.exception.SparkEndPointException;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.SparkException;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//TODO : this class only can be test entire with integration test
public class ExplorerSparkContex implements Connector<SparkContext> {

    private Logger logger = LoggerFactory.getLogger(ExplorerSparkContex.class);

    private final String PROPERTY_SPARK_MASTER ="spark.master";


    private SparkContext sc;

    /**
     * Load Spark context by properties
     * @param properties with configuration
     * @return this object
     */
    @Override
    public Connector loadConfiguration(Properties properties) {
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        FunctionalList<String,InetSocketAddress> functionalList = new FunctionalList<String,InetSocketAddress>(keys);

        String propertyValue =(String)properties.get(PROPERTY_SPARK_MASTER);
        checkPropertyMasterExist(propertyValue);
        checkPropertyIsNotempty(propertyValue);
        try {
            sc=  new SparkContext(new SparkConf()
                                       .setMaster(propertyValue)
                                        .setAppName("stratio-explorer"));
        }catch(Exception e){ //TODO : this is exception becaus
            String message = "Spark end point not valid or Spark is not upper";
            logger.error(message);
            throw new SparkEndPointException(new Exception(),message);
        }


        return this;
    }


    private List<SparkConf> genrateSparkConf(Properties properties){
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        FunctionalList<String,SparkConf> functionalList = new FunctionalList<String,SparkConf>(keys);
        functionalList.map()
    }


    private void checkPropertyMasterExist(String propertyValue){
        if (propertyValue==null)
            throw new NotPropertyFoundException(new Exception(),"Property "+PROPERTY_SPARK_MASTER+" no exist ");
    }

    private void checkPropertyIsNotempty(String propertyValue){
        if (propertyValue.isEmpty())
            throw new MasterPropertyNotFilledException("Property "+PROPERTY_SPARK_MASTER+ " not filled");
    }



    @Override
    public SparkContext getConnector() {
        return sc;
    }
}
