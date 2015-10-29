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

import com.stratio.explorer.lists.FunctionalList;
import com.stratio.explorer.spark.functions.SparkPropertyToSparkConf;
import com.stratio.explorer.checks.CheckerCollection;
import org.apache.spark.SparkConf;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Create Spark connector
 */
public class SparkConnectorCreator {


    private boolean isNewConnection;
    private Collection<SparkConf> connectorConfigurer = new ArrayList<>();
    private final String CT_MASTER ="spark.master";


    /**
     * set if conection is new .
     * @param newConnection
     */
    public void setNewConnection(boolean isNewConnection) {
        this.isNewConnection = isNewConnection;
    }

    /**
     * Build sparl context confgurer .
     * @param properties
     */
    public void buildConnections(Properties properties) {
        Collection<SparkConf> localConcatpoint = extractConectionFrom(properties);
        CheckerCollection checkerCollection = new CheckerCollection<InetSocketAddress>(" Host port property is not filled");
        checkerCollection.checkIsCollectionISNotEmpty(localConcatpoint);
        fillAttributtes(localConcatpoint);
    }


    private Collection<SparkConf> extractConectionFrom(Properties properties){
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        FunctionalList<String,SparkConf> functionalList = new FunctionalList<String,SparkConf>(keys);
        return functionalList.map(new SparkPropertyToSparkConf(properties));
    }

    private void fillAttributtes(Collection<SparkConf> localConcatpoint){
        if (notEquals(localConcatpoint)){
            isNewConnection = true;
            connectorConfigurer = new ArrayList<>(localConcatpoint);
        }
    }

    //TODO : THIS COMPARE IS BETTER WITH FACADES THAT REIMPLEMENT EQUALS AND HASH
    private boolean notEquals(Collection<SparkConf> localConcatpoint){
        List<SparkConf> firstList = new ArrayList<>(localConcatpoint);
        List<SparkConf> secondList = new ArrayList<>(connectorConfigurer);
        boolean equals = firstList.size() == secondList.size();
        for (int index=0;index<firstList.size() && equals;index++){
            equals = firstList.get(index).get(CT_MASTER).equals(secondList.get(index).get(CT_MASTER));
        }
        return !equals;
    }

    /**
     * Return is last connection is new .
     * @return true if las connection is new
     */
    public boolean isNewConnexionLoaded() {
        return isNewConnection;
    }

    /**
     *  Return all configured connectors .
      * @return all configured connectors
     */
    public Collection<SparkConf> getConnections() {
        return connectorConfigurer;
    }
}
