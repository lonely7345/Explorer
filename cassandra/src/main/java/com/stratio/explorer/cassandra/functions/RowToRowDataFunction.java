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

import com.datastax.driver.core.Row;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afidalgo on 14/10/15.
 */
public class RowToRowDataFunction implements TransformFunction<Row,RowData>{


    private FunctionalList<String,CellData> functionalList;

    /**
     * Constructor
     * @param header list of row names
     */
    public RowToRowDataFunction(List<String> header){

        this.functionalList = new FunctionalList<>(header);
    }

    /**
     * Transform Row to rowData
     * @param row to transfor
     * @return RowData object
     */
    @Override
    public RowData transform(Row row) {
         return new RowData(functionalList.map(new CellValueFunction(row)));

    }
}
