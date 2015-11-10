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
