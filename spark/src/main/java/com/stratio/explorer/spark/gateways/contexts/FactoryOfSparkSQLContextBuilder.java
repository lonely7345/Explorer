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

import com.stratio.explorer.spark.exception.PropertyHiveNotFilledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Must be create new SQLContextBuilder
 */
public class FactoryOfSparkSQLContextBuilder {

    private static Logger logger = LoggerFactory.getLogger(FactoryOfSparkSQLContextBuilder.class);

    public static String CT_HIVE_CONTEXT="YES";
    public static String CT_NO_HIVE_CONTEXT ="NO";


    private static final Map<String, SQLContextBuilder> builders = new HashMap<String, SQLContextBuilder>(){
        {
            put(CT_HIVE_CONTEXT, new SparkHiveContextBuilder());
            put(CT_NO_HIVE_CONTEXT, new SparkSQLContextBuilder());
        }
    };

    /**
     * Build new SQLContextBuilder .
     * @param propertyHive type Builder to build
     * @return SQLContextBuilder
     */
    public static  SQLContextBuilder get(String propertyHive){
            if (propertyHive==null){
                String message = "Not exist property spark.hiveContext";
                logger.error(message);
                throw  new PropertyHiveNotFilledException(message);
            }
            return builders.get(propertyHive);
    }

}
