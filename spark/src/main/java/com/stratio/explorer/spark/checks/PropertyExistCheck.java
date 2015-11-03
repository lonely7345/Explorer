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

package com.stratio.explorer.spark.checks;

import com.stratio.explorer.checks.PropertyChecker;
import com.stratio.explorer.exceptions.NotPropertyFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Check if proiperty is not null
 */
public class PropertyExistCheck implements PropertyChecker {

    private Logger logger = LoggerFactory.getLogger(PropertyExistCheck.class);


    /**
     * Check if proiperty is not null .
     * @param property to check
     * @param  propertyName
     */
    @Override
    public void check(String propertyName,String propertyValue) {
        if (propertyValue==null) {
            String message = "Property " + propertyName + " no exist ";
            logger.error(message);
            throw new NotPropertyFoundException(new Exception(), message);
        }
    }
}
