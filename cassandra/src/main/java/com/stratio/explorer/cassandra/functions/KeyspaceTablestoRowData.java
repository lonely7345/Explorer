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

package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform KeySpacetables to RowData with tableName and KeyspaceName
 */
public class KeyspaceTablestoRowData implements TransformFunction<KeyspaceMetadata,RowData> {


    /**
     *
     * @param keyspaceMetadata
     * @return ransform KeySpacetables to CellData with tableName
     */
    @Override
    public RowData transform(KeyspaceMetadata keyspaceMetadata) {

        List<TableMetadata> tables = new ArrayList<>(keyspaceMetadata.getTables());
        FunctionalList<TableMetadata,CellData> functional = new FunctionalList<>(tables);
        List<CellData> cells = new ListUtils<CellData>().buildList(new CellData(buildTable(keyspaceMetadata.getName(),functional)));
        return new RowData(cells);
    }


    private Table buildTable(String NameKeySpace,FunctionalList<TableMetadata,CellData> functional){
        List<String> header = new ListUtils<String>().buildList("Keyspace "+NameKeySpace);
        List<RowData> rows = new ListUtils<RowData>().buildList(new RowData(functional.map(new TableMetaDataToCellData())));
        return new Table(header,rows);
    }
}
