package com.stratio.notebook.cassandra.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afidalgo on 7/09/15.
 */
public class RowData {

    private List<CellData> cells = new ArrayList<>();

    public List<CellData> cells(){
         return cells;
    }

    public void addCell(CellData cell){
        cells.add(cell);
    }
}
