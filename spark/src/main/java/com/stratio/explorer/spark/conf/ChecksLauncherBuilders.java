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
package com.stratio.explorer.spark.conf;

import com.stratio.explorer.checks.PropertyChecker;
import com.stratio.explorer.checks.PropertyCheckerLauncher;
import com.stratio.explorer.spark.checks.*;

/**
 * Build checksLauncher with predefined configurer
 */
public class ChecksLauncherBuilders {


    /**
     * build propertyLauncher .
     * @return PropertyCheckerLauncher configured
     */
    public static PropertyCheckerLauncher checksLaunchertosparkProperties(){
        PropertyChecker propertyExistCheck = new PropertyExistCheck();
        PropertyChecker propertyNotEmptyCheck = new PropertyNotEmptyCheck();
        PropertyCorrectURLSparkCheck propertyCorrectURLSparkCheck = new PropertyCorrectURLSparkCheck("mesos", "local[*]");
        ConditionExecCheck condition = new ExecutablePropertyCondition(AttributteNames.CT_MASTER);
        return new PropertyCheckerLauncher().addCheck(propertyExistCheck)
                .addCheck(propertyNotEmptyCheck)
                .addCheck(new PropertyCheckWithCondition(propertyCorrectURLSparkCheck,condition));
    }
}
