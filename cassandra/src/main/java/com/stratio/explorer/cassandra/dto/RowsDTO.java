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
package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.CellData;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform rowData to DTO.
 */
public class RowsDTO {


    /**
     * Transform rowData to DTO with line separator
     * @param rows rowData to DTO.
     * @return DTO
     */
    public String toDTO(List<RowData> rows){
        List<String>  list = new ArrayList<>();
        for (RowData row:rows) {
            list.add(cellDto(row.cells()));
        }
        return StringUtils.join(list, System.getProperty("line.separator"));
    }

    private String cellDto(List<CellData> cells){
        List<Object> list = new ArrayList<>();
        for (CellData cell:cells) {
            CellDTO cellDTO = CellDTOFactory.getDTO(cell);
            list.add(cellDTO.toDTO(cell));
        }
        return StringUtils.join(list, StringConstants.TABULATOR) ;
    }

}
