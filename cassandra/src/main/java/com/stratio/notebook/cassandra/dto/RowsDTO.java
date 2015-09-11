package com.stratio.notebook.cassandra.dto;

import com.stratio.notebook.cassandra.models.CellData;
import com.stratio.notebook.cassandra.models.RowData;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afidalgo on 10/09/15.
 */
public class RowsDTO {


    public RowsDTO(){

    }

    public String toDTO(List<RowData> rows){
        List<String>  list = new ArrayList<>();
        for (RowData row:rows)
            list.add(cellDto(row.cells()));
        return StringUtils.join(list, "\n");
    }

    private String cellDto(List<CellData> cells){
        List<Object> list = new ArrayList<>();
        for (CellData cell:cells)
            list.add(cell.getValue().toString());
        return StringUtils.join(list,"\t") ;
    }
}
