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
import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.gateways.ConnectorCreator;
import com.stratio.explorer.lists.FunctionalList;
import com.stratio.explorer.spark.exception.SparkEndPointException;
import com.stratio.explorer.spark.functions.SparkPropertyToSparkConf;
import com.stratio.explorer.checks.CheckerCollection;
import com.stratio.explorer.spark.lists.SparkConfComparator;
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

    private ConnectorCreator<SparkConf> creator = new ConnectorCreator<SparkConf>(new SparkConfComparator()," Porperty spark master is not filled ");


    private SparkContext sc;

    /**
     * Load Spark context by properties
     * @param properties with configuration
     * @return this object
     */
    @Override
    public Connector loadConfiguration(Properties properties) {
            List<String> keysToInspect = new ArrayList<>(properties.stringPropertyNames());
            creator.buildConnections(keysToInspect,new SparkPropertyToSparkConf(properties));
            return this;
    }



    @Override
    public SparkContext getConnector() {
      try {
          if (creator.isNewConnexionLoaded()) {
              sc = new SparkContext(new ArrayList<>(creator.getConnections()).get(0));
              creator.setNewConnection(false);
          }
          return sc;
      }catch (UnsatisfiedLinkError e){
          String message = "Spark end point not valid or Spark is not upper";
          logger.error(message);
          throw new SparkEndPointException(e,message);
      }
    }
}
