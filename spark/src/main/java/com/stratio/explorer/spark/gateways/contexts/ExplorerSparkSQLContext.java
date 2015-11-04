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
import com.stratio.explorer.spark.exception.PropertyHiveContextValueException;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Create diferents types Spark context that support SQL
 */
public class ExplorerSparkSQLContext implements Connector<SQLContext> {


    private Logger logger = LoggerFactory.getLogger(ExplorerSparkSQLContext.class);

    private SQLContextBuilder builder;
    private Connector<SparkContext> sparkContext;


    /**
     * Constructor with
     * @param sparkContex
     */
    public ExplorerSparkSQLContext(Connector<SparkContext> sparkContex){
           this.sparkContext = sparkContex;
    }

    /**
     *  Load context that support SQL in Spark from properties .
     * @param properties with configuration
     * @return
     */
    @Override
    public Connector loadConfiguration(Properties properties) {

        String hiveProperty = (String)properties.getProperty("spark.hiveContext");
        builder = FactoryOfSparkSQLContextBuilder.get(hiveProperty);
        return this;
    }

    /**
     * Get last connector loaded .
     * @return last loaded connector
     */
    @Override
    public SQLContext getConnector() {
         try{
            return builder.build(sparkContext.getConnector());
         }catch (NullPointerException e){
            String message = "Property spark.hiveContext not have value correct";
            logger.error(message);
            throw  new PropertyHiveContextValueException(e,message);
         }
    }
}
