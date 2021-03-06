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
package com.stratio.explorer.cassandra.models;


import java.util.List;

public class Table {


    private List<String> header;
    private List<RowData> rows;

    /**
     * Constructor
     * @param header header of table
     * @param rows   row of table
     */
    public Table(List<String> header,List<RowData> rows){
        this.header = header;
        this.rows = rows;
    }

    /**
     *
     * @return all rows of table
     */
    public List<RowData> rows(){
         return rows;
    }


    /**
     *
     * @return header of table
     */
    public List<String> header(){
        return header;
    }
}
