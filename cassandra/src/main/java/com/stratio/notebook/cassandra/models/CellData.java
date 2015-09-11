package com.stratio.notebook.cassandra.models;


public class CellData<K> {

    private K value;

    public CellData(K value){
       this.value = value;
    }


    public K getValue() {
        return value;
    }
}
