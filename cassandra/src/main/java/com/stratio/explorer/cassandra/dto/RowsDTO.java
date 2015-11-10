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
        for (RowData row:rows)
            list.add(cellDto(row.cells()));
        return StringUtils.join(list, System.getProperty("line.separator"));
    }

    private String cellDto(List<CellData> cells){
        List<Object> list = new ArrayList<>();
        for (CellData cell:cells)
            list.add(cell.getValue().toString());
        return StringUtils.join(list, StringConstants.TABULATOR) ;
    }
}
