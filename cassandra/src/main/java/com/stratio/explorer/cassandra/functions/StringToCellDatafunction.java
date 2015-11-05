package com.stratio.explorer.cassandra.functions;

import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;

//TODO : THIS CLASS WILL BE REMOVED
/**
 * Transform Srting in CellData
 */
public class StringToCellDatafunction implements TransformFunction<String,CellData>{


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
