package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.functions.TransformFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform TableMetada in RowData
 */
public class TableMetadataToRowDataFunction implements TransformFunction<TableMetadata,RowData>{


    /**
     * Transform TableMetada in RowData .
     * @param objetcToTransform input object
     * @return
     */
    @Override
    public RowData transform(TableMetadata tableMetadata) {
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(tableMetadata.toString()));
        return new RowData(cells);
    }



}
