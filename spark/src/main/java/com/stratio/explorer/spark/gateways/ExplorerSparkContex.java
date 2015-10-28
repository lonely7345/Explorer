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
import com.stratio.explorer.spark.exception.MasterPropertyNotCorrectException;
import org.apache.spark.SparkContext;

import java.util.Properties;

//TODO : this class only can be test entire with integration test
public class ExplorerSparkContex implements Connector<SparkContext> {



    private final String PROPERTY_SPARK_MASTER ="spark.master";

    @Override
    public Connector loadConfiguration(Properties properties) {

        String propertyValue =(String)properties.get(PROPERTY_SPARK_MASTER);
        checkPropertyMasterExist(propertyValue);
        checkPropertyIsNotempty(propertyValue);

        return this;
    }


    private void checkPropertyMasterExist(String propertyValue){
        if (propertyValue==null)
            throw new NotPropertyFoundException(new Exception(),"Property "+PROPERTY_SPARK_MASTER+" no exist ");
    }

    private void checkPropertyIsNotempty(String propertyValue){
        if (propertyValue.isEmpty())
            throw new MasterPropertyNotCorrectException("Property "+PROPERTY_SPARK_MASTER+ " not filled");
    }





    @Override
    public SparkContext getConnector() {
        return null;
    }
}
