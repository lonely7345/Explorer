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

package com.stratio.explorer.spark.functions;

import com.stratio.explorer.functions.SearcherFunction;
import org.apache.spark.SparkConf;

/**
 * Compare two SparkConf
 */
public class CompareSparkConf implements SearcherFunction<SparkConf> {

    private SparkConf sparkConf;
    private String master;

    /**
     * Contructor .
     * @param sparkConf sparkConf to compare.
     * @param master parameter to compare
     */
    public CompareSparkConf(SparkConf sparkConf, String master){
        this.sparkConf = sparkConf;
        this.master = master;
    }

    /**
     * Compare parameter conf with sparkConf
     * @param scCompare
     * @return
     */
    @Override
    public boolean isValid(SparkConf scCompare) {
        return scCompare.get(master).equals(sparkConf.get(master));
    }
}