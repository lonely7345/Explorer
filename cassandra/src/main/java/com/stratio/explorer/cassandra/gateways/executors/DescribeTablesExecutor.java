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
import com.stratio.explorer.cassandra.functions.KeyspaceTablestoRowData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.List;

/**
 * Execute DESCRIBE TABLES .
 */
public class DescribeTablesExecutor implements DescribeExecutor{


    public static final String NAME_KEYSPACE = "Name KeySpace";
    public static final String NAME_TABLE ="Name Table";

    public static final String WORD_SELECTOR ="TABLES";


    /**
     * Is not mandatory
     * @param param
     */
    @Override
    public void optionalParam(String param) {
        //lefty empty deliverely
    }


    /**
     * Execute DESCRIBE TABLES .
     * @param metaData
     * @return table
     */
    @Override
    public Table execute(Metadata metaData) {
        FunctionalList<KeyspaceMetadata,RowData> functional = new FunctionalList<>( metaData.getKeyspaces());
        List<RowData> rows = functional.map(new KeyspaceTablestoRowData());
        return new Table(new ListUtils<String>().buildList(), rows);
    }
}
