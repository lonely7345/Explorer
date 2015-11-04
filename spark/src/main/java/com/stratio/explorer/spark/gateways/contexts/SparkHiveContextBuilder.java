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
package com.stratio.explorer.spark.gateways.contexts;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.hive.HiveContext;

/**
 * Must build types of SparkSQL context
 */
public class SparkHiveContextBuilder implements SQLContextBuilder<HiveContext>{

    /**
     * Build an HiveContext type .
     * @param sparkContext to configure HiveContext
     * @return HiveContext
     */
    @Override
    public HiveContext build(SparkContext sparkContext) {
        return new HiveContext(sparkContext);
    }
}
