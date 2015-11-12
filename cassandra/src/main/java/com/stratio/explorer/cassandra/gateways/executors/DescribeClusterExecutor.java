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

package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.NameValueToRowData;
import com.stratio.explorer.cassandra.models.NameValue;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.List;

/**
 * Excute DESCRIBE CLUSTER shCQL
 */
public class DescribeClusterExecutor implements DescribeExecutor{

    public static final String CT_CLUSTER ="Cluster";
    public static final String CT_PARTIRIONER ="Partitioner";
    public static final String WORD_SELECTOR ="CLUSTER";

    private Metadata metaData;

    /**
     * In this class is not mandatory
     * @param param
     */
    public void optionalParam(String param){
        //left empty deliverely
    }


    /**
     * Excute DESCRIBE CLUSTER
     * @param params attributtes describe
     * @return
     */
    @Override
    public Table execute(Metadata metaData) {
        List<NameValue> list = new ListUtils<NameValue>().buildList(new NameValue(CT_CLUSTER, metaData.getClusterName()),
                                                                     new NameValue(CT_PARTIRIONER, metaData.getPartitioner()));
        FunctionalList<NameValue,RowData> functionalList =
                new FunctionalList<NameValue,RowData>(list);

        List<RowData> data = functionalList.map(new NameValueToRowData());
        return new Table(new ListUtils<String>().buildList(),data);
    }





}
