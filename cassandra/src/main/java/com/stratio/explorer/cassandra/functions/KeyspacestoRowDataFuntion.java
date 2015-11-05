package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.functions.TransformFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform KeySpace to row Data
 */
public class KeyspacestoRowDataFuntion implements TransformFunction<KeyspaceMetadata,RowData>{



    /**
     * Transform KeyspaceMetadata to RowData
     * @param objetcToTransform input object
     * @return
     */
    @Override
    public RowData transform(KeyspaceMetadata keyspaceMetadata) {
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(keyspaceMetadata.getName()));
        return new RowData(cells);
    }
}
