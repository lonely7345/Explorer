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
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.is;


public class CellTableDTOTest {


    @Test
    public void whenCellContainsTable(){
       String keySpace ="keySpace";
       String cellValueOne ="cellValueOne";
       String cellValueTwo ="cellValueTwo";
       CellData<Table> cellWithTable = new CellData<Table>(buildTable(keySpace,cellValueOne,cellValueTwo));
       CellTableDTO cellDTO = new CellTableDTO();
       String dto =cellDTO.toDTO(cellWithTable);
       assertThat("Result should be table formatted to TO",dto,is( "keySpace" + System.getProperty("line.separator") + cellValueOne + StringConstants.TABULATOR + cellValueTwo));

    }

    private Table buildTable(String headerValue,String... cellValues){
        List<String> header =new ListUtils<String>().buildList(headerValue);
        List<CellData> cells = new ListUtils<CellData>().buildList(new CellData(cellValues[0]),new CellData(cellValues[1]));
        List<RowData> rows = new ListUtils<RowData>().buildList(new RowData(cells));
        return new Table(header,rows);
    }
}
