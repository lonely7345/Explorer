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

package com.stratio.explorer.spark.functions;


import com.stratio.explorer.checks.PropertyCheckerLauncher;
import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.spark.checks.PropertyCorrectURLSparkCheck;
import com.stratio.explorer.spark.checks.PropertyExistCheck;
import com.stratio.explorer.spark.checks.PropertyNotEmptyCheck;
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * contain tranform that transfor one property in sparkConf
 */
public class SparkPropertyToSparkConf implements TransformFunction<String,SparkConf> {

    private Logger logger = LoggerFactory.getLogger(SparkPropertyToSparkConf.class);
    private Properties properties;

    private final String CT_APP_NAME = "stratio-explorer";

    private PropertyCheckerLauncher runnerPropertiesCheck = new PropertyCheckerLauncher();

    /**
     * Constructor with SparkContex configuration
     *
     * @param properties
     */
    public SparkPropertyToSparkConf(Properties properties) {
        this.properties = properties;
        runnerPropertiesCheck.addCheck(new PropertyExistCheck())
                .addCheck(new PropertyNotEmptyCheck())
                .addCheck(new PropertyCorrectURLSparkCheck("mesos"));

    }


    /**
     * Transform propertyName into file properties in SparkConf.
     *
     * @param propertyName
     * @return SparkConf
     */
    @Override
    public SparkConf transform(String propertyName) {
        String valueProperty = (String) properties.get(propertyName);
        runnerPropertiesCheck.runAllChecks(valueProperty);
        return new SparkConf()
                .setMaster(valueProperty)
                .setAppName(CT_APP_NAME);
    }
}
