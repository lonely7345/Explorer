package com.stratio.explorer.cassandra.models;

/**
 * Bean with name And value .
 */
public class NameValue {


    private String name,value;


    /**
     * Constructor with name and value .
     * @param name
     * @param value
     */
    public NameValue(String name,String value){
        this.name = name;
        this.value = value;
    }

    /**
     * Obtain name
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Obtain Value .
     * @return value
     */
    public String getValue(){
        return value;
    }

}
