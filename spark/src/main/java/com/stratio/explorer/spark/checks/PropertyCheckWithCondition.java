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
package com.stratio.explorer.spark.checks;

import com.stratio.explorer.checks.PropertyChecker;

/**
 * Check must be execute if condition complain
 */
public class PropertyCheckWithCondition implements PropertyChecker {

     private PropertyChecker checker;
     private ConditionExecCheck condition;


    /**
     * Constructor
      * @param checker to execute
     * @param condition to execute checker
     */
    public PropertyCheckWithCondition(PropertyChecker checker,ConditionExecCheck condition){
         this.checker = checker;
         this.condition = condition;
     }

    /**
     * Execute check if condition is true
     * @param propertyValue
     */
    @Override
    public void check(String propertyName,String propertyValue) {
        if (condition.isExecutable(propertyName)) {
            checker.check(propertyName,propertyValue);
        }
    }
}
