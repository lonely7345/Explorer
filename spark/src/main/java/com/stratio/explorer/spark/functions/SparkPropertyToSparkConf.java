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


import com.stratio.explorer.checks.PropertyCheckerLauncher;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.spark.checks.*;
import com.stratio.explorer.spark.conf.AttributteNames;
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
    private SparkConf sparkConf = new SparkConf().setAppName(CT_APP_NAME);

    /**
     * Constructor with SparkContex configuration
     *
     * @param properties
     * @param sparkConf initial configuration
     */
    public SparkPropertyToSparkConf(Properties properties) {
        this.properties = properties;
        runnerPropertiesCheck.addCheck(new PropertyExistCheck())
                .addCheck(new PropertyNotEmptyCheck())
                .addCheck(new PropertyCheckWithCondition(new PropertyCorrectURLSparkCheck("mesos", "local[*]"),new ExecutablePropertyCondition(AttributteNames.CT_MASTER)));

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
        runnerPropertiesCheck.runAllChecks(propertyName,valueProperty);
        sparkConf.set(propertyName,valueProperty);
        return sparkConf;
    }
}
