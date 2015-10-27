package com.stratio.explorer.cassandra.dto;


import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.Table;
import org.apache.commons.lang.StringUtils;


public class TableDTO {

     public String toDTO(Table table) {

         String message = StringConstants.TYPE_TEXT+StringConstants.OPERATION_OK;;
         if (!table.header().isEmpty()) {
             message = StringConstants.TYPE_TABLE + StringUtils.join(table.header(), StringConstants.TABULATOR) + System.getProperty("line.separator") + new RowsDTO().toDTO(table.rows());
         }
         return message;
    }

}
