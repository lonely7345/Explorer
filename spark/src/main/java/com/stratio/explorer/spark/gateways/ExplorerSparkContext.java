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

import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.lists.FunctionalList;
import com.stratio.explorer.spark.exception.SparkEndPointException;
import com.stratio.explorer.spark.functions.SparkPropertyToSparkConf;
import com.stratio.explorer.checks.CheckerCollection;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Create a Spark Context
 */
public class ExplorerSparkContext implements Connector<SparkContext> {

    private Logger logger = LoggerFactory.getLogger(ExplorerSparkContext.class);

    private final String PROPERTY_SPARK_MASTER ="spark.master";


    private SparkContext sc;

    /**
     * Load Spark context by properties
     * @param properties with configuration
     * @return this object
     */
    @Override
    public Connector loadConfiguration(Properties properties) {

        String propertyValue =(String)properties.get(PROPERTY_SPARK_MASTER);
        List<SparkConf> confs =  genrateSparkConf(properties);
        CheckerCollection checkerCollection = new CheckerCollection<SparkConf>("Porperty "+PROPERTY_SPARK_MASTER+ "no exist");
        checkerCollection.checkIsCollectionISNotEmpty(confs);
        buildSparkContext(confs.get(0));
        return this;
    }


    private List<SparkConf> genrateSparkConf(Properties properties){
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        FunctionalList<String,SparkConf> functionalList = new FunctionalList<String,SparkConf>(keys);
        return  functionalList.map(new SparkPropertyToSparkConf(properties));
    }


    private void buildSparkContext(SparkConf sparkConf){
        try {
            sc=  new SparkContext(sparkConf);
        }catch(UnsatisfiedLinkError e){
            String message = "Spark end point not valid or Spark is not upper";
            logger.error(message);
            throw new SparkEndPointException(new Exception(),message);
        }
    }


    @Override
    public SparkContext getConnector() {
        return sc;
    }
}
