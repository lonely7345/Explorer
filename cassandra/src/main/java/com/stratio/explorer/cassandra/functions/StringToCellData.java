package com.stratio.explorer.cassandra.functions;


import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;

/**
 * Transform Object in CellData
 */
public class StringToCellData implements TransformFunction<String,CellData>{


    /**
     * Transform value in CellData
     * @param value
     * @return
     */
    @Override
    public CellData transform(String value) {
        return new CellData(value);
    }


}
