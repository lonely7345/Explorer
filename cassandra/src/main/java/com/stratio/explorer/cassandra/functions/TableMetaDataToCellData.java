package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.functions.TransformFunction;

/**
 * Transform TableMetadata to CellData
 */
public class TableMetaDataToCellData implements TransformFunction<TableMetadata,CellData> {

    /**
     *  Transform TableMetadata to CellData with TableName Value
     * @param value
     * @return CellData with Table Name valie
     */
    @Override
    public CellData transform(TableMetadata value) {
        return new CellData(value.getName());
    }
}