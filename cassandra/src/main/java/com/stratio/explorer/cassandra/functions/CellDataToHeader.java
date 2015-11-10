package com.stratio.explorer.cassandra.functions;

import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;

/**
 * Transform Celldata to String Header
 */
public class CellDataToHeader implements TransformFunction<CellData,String>{


    private String initial;
    private String next;
    private boolean first = true;


    /**
     * Constructor with inital header and next Header repeater
     * @param initial
     * @param next
     */
    public CellDataToHeader(String initial,String next){
        this.initial = initial;
        this.next = next;
    }

    @Override
    public String transform(CellData value) {
        String result =  next;
        if (first){
            first = false;
            result = initial;
        }
        return result;
    }
}
