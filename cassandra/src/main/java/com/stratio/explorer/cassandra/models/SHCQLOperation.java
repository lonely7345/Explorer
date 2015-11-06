package com.stratio.explorer.cassandra.models;

/**
 * bean with values of describe operation
 */

//TODO : INCLUDE JAVADOC
public class SHCQLOperation {

    private String [] describe;

    public SHCQLOperation(String shCQLcommand){
        describe = shCQLcommand.split(" +");;
    }

    public String nameOperation(){
        return describe[0];
    }

    public String identifier(){
        return describe[1];
    }

    //TODO : THIS METHOD WILL NEED THROW EXCEPTION
    public String optionalValue(){
        String result ="";
        if (describe.length>2)
            result = describe[2];
        return result;
    }
}
