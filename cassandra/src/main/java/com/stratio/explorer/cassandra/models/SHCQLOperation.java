package com.stratio.explorer.cassandra.models;

/**
 * bean with values of describe operation
 */
public class SHCQLOperation {

    private String [] describe;

    /**
     * Constructor of shCQL command .
     * @param shCQLcommand
     */
    public SHCQLOperation(String shCQLcommand){
        describe = shCQLcommand.split(" +");;
    }

    /**
     * name of shCql operation .
     * @return name of shCql operation
     */
    public String nameOperation(){
        return describe[0];
    }

    /**
     * identifier of Describe (table,Tables,KeySpace,KeySpaces) .
     * @return identifier of Describe
     */
    public String identifier(){
        return describe[1].replaceAll(";","").trim();
    }

    /**
     * Recovery optional value of Describe
     * @return optional Value
     */
    public String optionalValue(){
        String result ="";
        if (describe.length>2)
            result = describe[2];
        return result;
    }
}
