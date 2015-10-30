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

package com.stratio.explorer.spark.lists;

import com.stratio.explorer.lists.CollectionsComparator;
import com.stratio.explorer.lists.FunctionalList;
import com.stratio.explorer.spark.functions.CompareSparkConf;
import org.apache.spark.SparkConf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Compare collections of SparkConf
 */
public class SparkConfComparator implements CollectionsComparator<SparkConf>{


    private final String CT_MASTER ="spark.master";

    /**
     * Compare two collections
     * @param firstCollection
     * @param secondCollection
     * @return true if two collections ar not equals
     */
    @Override
    public boolean notEquals(Collection<SparkConf> firstCollection, Collection<SparkConf> secondCollection) {

        FunctionalList<SparkConf,String> firstList = new FunctionalList<SparkConf,String>(new ArrayList<SparkConf>(firstCollection));
        List<SparkConf> secondList = new ArrayList<>(secondCollection);
        boolean equals = firstList.size() == secondList.size();
        for (int index=0;index<secondList.size() && equals;index++){
            equals = !firstList.search(new CompareSparkConf(secondList.get(index),CT_MASTER)).isEmpty();
        }
        return !equals;
    }
}
