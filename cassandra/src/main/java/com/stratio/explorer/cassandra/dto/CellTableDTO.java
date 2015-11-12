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

import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.Table;
import org.apache.commons.lang.StringUtils;

/**
 * Transform to DTO cell with Table value
 */
public class CellTableDTO implements CellDTO {


    /**
     * Transform to DTO cell with Table value.
     * @param cellWithTable
     * @return DTO of table
     */
    @Override
    public String toDTO(CellData cellWithTable) {

        Table table = (Table)cellWithTable.getValue();
        String noType ="";
        return new TableDTO(noType).toDTO(table);
    }
}
