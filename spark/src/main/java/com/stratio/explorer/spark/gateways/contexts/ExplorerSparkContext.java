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
import com.stratio.explorer.gateways.ConnectorCreator;
import com.stratio.explorer.lists.CollectionsComparator;
import com.stratio.explorer.spark.conf.MadatoryPropertiesList;
import com.stratio.explorer.spark.exception.SparkEndPointException;
import com.stratio.explorer.spark.functions.SparkPropertyToSparkConf;
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
    private final String CT_MESSAGE_ERROR_MASTER_NOT_FILLED =" Porperty spark master is not filled ";
    private final String PROPERTY_SPARK_MASTER ="spark.master";

    private ConnectorCreator<SparkConf> creator;
    private SparkContext sc;


    /**
     * Constructor with SparkConfComparator , with this attributte all nez configuratiosn will be compared to know if a new configuration
     * @param configuredAttributesCollection
     */
    public ExplorerSparkContext(CollectionsComparator<SparkConf> configuredAttributesCollection){
        creator = new ConnectorCreator<SparkConf>(configuredAttributesCollection,CT_MESSAGE_ERROR_MASTER_NOT_FILLED);
    }

    /**
     * Load Spark context by properties
     * @param properties with configuration
     * @return this object
     */
    @Override
    public Connector loadConfiguration(Properties properties) {
            List<String> keysToInspect = MadatoryPropertiesList.buildMandatoryPropertiesListToBuildSparkContext();
            creator.buildConnections(keysToInspect,new SparkPropertyToSparkConf(properties));
            return this;
    }


    /**
     * Return last SparkContext loaded .
     * @return last sparkContext loaded
     */
    @Override
    public SparkContext getConnector() {
      try {
          if (creator.isNewConnexionLoaded()) {
              sc = new SparkContext(new ArrayList<>(creator.getConnections()).get(0));
              creator.setNewConnection(false);
          }
          return sc;
      } catch (UnsatisfiedLinkError e){
          String message = "Spark end point not valid or Spark is not upper";
          logger.error(message);
          throw new SparkEndPointException(e,message);
      }
    }
}
