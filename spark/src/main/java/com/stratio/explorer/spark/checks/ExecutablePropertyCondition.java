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

/**
 * Must validate if property check is executable
 */
public class ExecutablePropertyCondition implements ConditionExecCheck{


    private String propertyNameExecutable;

    /**
     * Constructor with name of property executable check
     * @param propertyNameexecutable
     */
    public ExecutablePropertyCondition(String propertyNameExecutable){
        this.propertyNameExecutable = propertyNameExecutable;
    }
    /**
     * Test if propertyName check is executable
     * @param propertyName
     * @return true if propertyName check is executable
     */
    @Override
    public boolean isExecutable(String propertyName) {
        return propertyNameExecutable.equals(propertyName);
    }
}
