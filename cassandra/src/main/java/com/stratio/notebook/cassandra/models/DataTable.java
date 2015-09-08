package com.stratio.notebook.cassandra.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afidalgo on 7/09/15.
 */
public class DataTable {

    private List<TableRow> rows = new ArrayList<>();

    public List<TableRow> rows(){
         return rows;
    }

    public void addRow(TableRow row){
        rows.add(row);
    }
}
