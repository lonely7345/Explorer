package com.stratio.notebook.cassandra.dto;


import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.Table;
import org.apache.commons.lang.StringUtils;


public class TableDTO {

     public String toDTO(Table table) {

         String message = StringConstants.TYPE_TEXT+StringConstants.OPERATION_OK;;
         if (!table.header().isEmpty())
             message =StringConstants.TYPE_TABLE+ StringUtils.join(table.header(),"\t")+"\n"+new RowsDTO().toDTO(table.rows());
         return message;
    }

}
