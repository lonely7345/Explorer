package com.stratio.explorer.cassandra.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afidalgo on 7/09/15.
 */
public class RowData {

    private List<CellData> cells = new ArrayList<>();


    /**
     * Constructor
     * @param cells list cells of row
     */
    public RowData(List<CellData> cells ){
        this.cells = cells;
    }

    /**
     *
     * @return cells of Row
     */
    public List<CellData> cells(){
         return cells;
    }

}
