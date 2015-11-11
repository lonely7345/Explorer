package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.models.CellData;

/**
 * Transform normal Cells toDTO
 */
public class CellStringDTO implements CellDTO{


    /**
     * Transform normal Cells toDTO.
     * @param cellWithTable
     * @return DTO
     */
    @Override
    public String toDTO(CellData cell) {
        String value = (String)cell.getValue();
        ScriptFormatter formatter = ScriptFormatterFactory.getFormatterTo(type(value));
        return formatter.format(value);

    }


    private String type(String value){
        String [] separated = value.split(" ");
        return separated[0];
    }
}
