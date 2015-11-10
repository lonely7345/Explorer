package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Select type of DTO
 */
public class TableDTOSelector {

    /**
     * Create String DTO depends type of table .
     * @param table to transform DTO
     * @return  String DTO (with type , text , table ...)
     */
    public String toDTO(Table table) {
        if (table.header().isEmpty())
           return new TableDTO(StringConstants.TYPE_TEXT).toDTO(table);
        return new TableDTO(StringConstants.TYPE_TABLE).toDTO(table);
    }
}
