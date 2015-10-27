/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.Row;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;

/**
 * Created by afidalgo on 14/10/15.
 */
public class CellValueFunction implements TransformFunction<String,CellData>{

    private Row row;


    /**
     * Constructor.
     * @param row row to transforms cell in cellData
     */
    public CellValueFunction(Row row){
        this.row = row;
    }



    /**
     * Transform cell with header name in CellData.
     * @param header neme in ros
     * @return CellDataObject
     */
    @Override
    public CellData transform(String header) {
        return new CellData(row.getObject(header));
    }
}
