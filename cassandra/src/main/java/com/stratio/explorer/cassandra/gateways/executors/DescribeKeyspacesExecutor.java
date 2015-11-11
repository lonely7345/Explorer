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


import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.KeyspacestoRowDataFuntion;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;


/**
 * Execute Describe Keysapces
 */
public class DescribeKeyspacesExecutor implements DescribeExecutor{

    private Metadata metaData;

    public static final String CT_KEYSPACES ="keySpaces";
    public static final String WORD_SELECTOR="KEYSPACES";


    /**
     * not mandoatory param .
     * @param param
     */
    public void optionalParam(String param){
       //left empty deliveraly
    }

    /**
     * Execute Describe Keysapces
     * @param metaData
     * @return  table
     */
    @Override
    public Table execute(Metadata metaData) {
        FunctionalList<KeyspaceMetadata,RowData> functional = new FunctionalList<>(metaData.getKeyspaces());
        List<RowData> rowDatas = functional.map(new KeyspacestoRowDataFuntion());
        return new Table(new ListUtils<String>().buildList(), rowDatas);
    }
}
