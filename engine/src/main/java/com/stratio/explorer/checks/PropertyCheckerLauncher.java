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
package com.stratio.explorer.checks;


import java.util.ArrayList;
import java.util.List;

/**
 * Launch all configured checks in property
 */
public class PropertyCheckerLauncher {



    private List<PropertyChecker> list = new ArrayList<>();

    /**
     * Add new checker to run .
     * @param checker checker to run
     * @return  this object
     */
    public PropertyCheckerLauncher addCheck(PropertyChecker checker){
        list.add(checker);
        return this;
    }


    /**
     * Run all configured checks , if any fail throw error
     * @param property property to check
     */
    public void runAllChecks(String propertyName,String property){
        for (PropertyChecker checker:list){
            checker.check(propertyName,property);
        }
    }
}
