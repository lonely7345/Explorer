package com.stratio.explorer.cassandra.models;


import java.util.List;

public class Table {


    private List<String> header;
    private List<RowData> rows;

    /**
     * Constructor
     * @param header header of table
     * @param rows   row of table
     */
    public Table(List<String> header,List<RowData> rows){
        this.header = header;
        this.rows = rows;
    }

    /**
     *
     * @return all rows of table
     */
    public List<RowData> rows(){
         return rows;
    }


    /**
     *
     * @return header of table
     */
    public List<String> header(){
        return header;
    }
}
