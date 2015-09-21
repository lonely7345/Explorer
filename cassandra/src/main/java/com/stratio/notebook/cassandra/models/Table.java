package com.stratio.notebook.cassandra.models;


import java.util.ArrayList;
import java.util.List;

public class Table {


    private List<String> header = new ArrayList<>();
    private List<RowData> rows= new ArrayList<>();

    public List<RowData> rows(){
         return rows;
    }

    public void addRow(RowData row){
        rows.add(row);

    }

    public void addHeaderParameter(String headerParam){
        header.add(headerParam);
    }

    public List<String> header(){
        return header;
    }
}
