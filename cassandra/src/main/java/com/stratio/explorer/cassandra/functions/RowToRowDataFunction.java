package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.Row;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.FunctionalList;

import java.util.List;

/**
 * Created by afidalgo on 14/10/15.
 */
public class RowToRowDataFunction implements TransformFunction<Row,RowData>{


    private List<String> header;

    private FunctionalList<String,CellData> functionalList;

    /**
     * Constructor
     * @param header list of row names
     */
    public RowToRowDataFunction(List<String> header){
        this.functionalList = new FunctionalList<>(header);
    }

    /**
     * Transform Row to rowData
     * @param row to transfor
     * @return RowData object
     */
    @Override
    public RowData transform(Row row) {
        RowData rowData = new RowData(functionalList.map(new CellValueFunction(row)));
        return rowData;
    }
}
