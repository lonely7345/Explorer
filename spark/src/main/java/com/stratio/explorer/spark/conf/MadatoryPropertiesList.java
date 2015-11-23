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

import java.util.ArrayList;
import java.util.List;

/**
 * Must buil list with mandatory properties in file
 */
public class MadatoryPropertiesList {

    /**
     * List manadatory attrbuttes that must be in conf file to create SparkContext
     * @return List with attributtes
     */
    public static List<String> buildMandatoryPropertiesListToBuildSparkContext(){
        List<String> returned = new ArrayList<>();
        returned.add(AttributteNames.CT_MASTER);
        returned.add(AttributteNames.CT_DRIVER_MEMORY);
        returned.add(AttributteNames.CT_EXECUTOR_MEMORY);
        returned.add(AttributteNames.CT_CORES);
        return returned;
    }
}
