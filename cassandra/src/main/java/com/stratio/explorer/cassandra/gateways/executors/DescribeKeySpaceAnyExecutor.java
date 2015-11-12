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
import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.functions.TableMetadataToRowDataFunction;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Execute DECRIBE KEYSPACE any
 */
public class DescribeKeySpaceAnyExecutor implements DescribeExecutor{



    public static final String WORD_SELECTOR="KEYSPACE";
    private String param;


    /**
     * Last param of describe keyspaces
     * @param param
     */
    public void optionalParam(String param){
        this.param = param;
    }

    /**
     *  Execute when shCQL is DESCRIBE KEYSPACE anyvalue
     *  @param metaData
     * @return  table
     */
    @Override
    public Table execute(Metadata metaData) {
        KeyspaceMetadata keySpaceMetada = metaData.getKeyspace(param);
        FunctionalList<TableMetadata,RowData> functionalList = new FunctionalList<>(new ArrayList<>(keySpaceMetada.getTables()));
        List<RowData> rows = functionalList.map(new TableMetadataToRowDataFunction());
        rows.add(0,buildFirst(keySpaceMetada.toString()));
        return new Table(new ListUtils<String>().buildList(),rows);
    }

    private RowData buildFirst(String valueuniqueCell){
        return new RowData(new ListUtils<CellData>().buildList(new CellData(valueuniqueCell)));
    }


}
