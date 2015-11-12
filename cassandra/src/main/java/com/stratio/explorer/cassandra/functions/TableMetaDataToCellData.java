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

package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.functions.TransformFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform TableMetadata to CellData
 */
public class TableMetaDataToCellData implements TransformFunction<TableMetadata,CellData> {

    /**
     *  Transform TableMetadata to CellData with TableName Value
     * @param value
     * @return CellData with Table value
     */
    @Override
    public CellData transform(TableMetadata value) {

        return new CellData(value.getName());
    }

    private Table buildTable(TableMetadata value){
        List<String> header = new ListUtils<String>().buildList(value.getKeyspace().getName());
        List<CellData> cells = new ListUtils<CellData>().buildList(new CellData(value.getName()));
        List<RowData> rows = new ListUtils<RowData>().buildList(new RowData(cells));
        return  new Table(header,rows);
    }

}