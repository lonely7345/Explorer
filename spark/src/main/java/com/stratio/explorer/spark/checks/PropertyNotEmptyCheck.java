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
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  check property is not empty
 */
public class PropertyNotEmptyCheck implements PropertyChecker {

    private Logger logger = LoggerFactory.getLogger(PropertyNotEmptyCheck.class);

    /**
     * Check property not empty ,if is empty throw error
     * @param property to check
     */
    @Override
    public void check(String propertyValue) {
        if (propertyValue.isEmpty()) {
            String message = "Property " + propertyValue + " not filled";
            logger.error(message);
            throw new MasterPropertyNotFilledException(message);
        }
    }
}
