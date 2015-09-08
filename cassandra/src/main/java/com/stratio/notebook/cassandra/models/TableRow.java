package com.stratio.notebook.cassandra.models;


public class TableRow<K> {

    private String type="";
    private String name="";
    private K value;


    public void attributeType(String type) {
           this.type =type;
    }

    public void attributeName(String name) {
           this.name = name;
    }

    public void attributeValue(K value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public K getValue() {
        return value;
    }
}
