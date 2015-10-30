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
package com.stratio.explorer.cassandra.dto;


import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.Table;
import org.apache.commons.lang.StringUtils;

/**
 * Transform Table toDTO
 */
public class TableDTO {


    private String typeDTO;

    /**
     * Constructor with typeDTO
     * @param typeDTO type DTO (TEXT,TABLE)
     */
    public TableDTO(String typeDTO){
           this.typeDTO = typeDTO;
    }

    /**
     *  Transform Table toDTO table visualize.
      * @param table Table
     * @return  String with table visualize.
     */
    public String toDTO(Table table) {


          String message = typeDTO + StringUtils.join(table.header(), StringConstants.TABULATOR) + System.getProperty("line.separator") + new RowsDTO().toDTO(table.rows());
         return message;
    }

}
