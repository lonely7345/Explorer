package com.stratio.explorer.cassandra.models;


public class CellData<K> {

    private K value;

    /**
     * Constructor
     * @param value cell
     */
    public CellData(K value){
       this.value = value;
    }


    /**
     *
     * @return value of cell
     */
    public K getValue() {
        return value;
    }
}
