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
import com.stratio.explorer.spark.exception.MalformedSparkURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Check URL is not malformed
 */
public class PropertyCorrectURLSparkCheck implements PropertyChecker {

    private Logger logger = LoggerFactory.getLogger(PropertyCorrectURLSparkCheck.class);

    private final String CT_SEPARATOR_START_URL ="://";
    private List<String> startedURL;

    /**
     * Constructor with valid started URLs
     * @param initialsURL
     */
    public PropertyCorrectURLSparkCheck(String... startedURL){
        this.startedURL = Arrays.asList(startedURL);
    }
    /**
     * Check property value have valid SPARK  url
     * @param propertyValue
     */
    @Override
    public void check(String propertyValue) {
        String [] urls = propertyValue.split(",");

        for (String url : urls){
            if (isNotValid(url))  {
                String message = "spark url "+url+" is not valid";
                logger.error(message);
                throw new MalformedSparkURLException(message);
            }
        }
    }


    private boolean isNotValid(String url){
        boolean result = false;
        for (int index=0;index<startedURL.size() && !result;index++){
            result = url.startsWith(startedURL.get(index)+CT_SEPARATOR_START_URL);
        }
        return !result;
    }
}
