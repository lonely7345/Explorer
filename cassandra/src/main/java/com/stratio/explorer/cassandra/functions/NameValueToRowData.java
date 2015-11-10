package com.stratio.explorer.cassandra.functions;

import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.NameValue;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.functions.TransformFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform NameValue to RowData
 */
public class NameValueToRowData implements TransformFunction<NameValue,RowData>{

    /**
     * Transform NameValue to RowData.
     * @param objetcToTransform input object
     * @return
     */
    @Override
    public RowData transform(NameValue value) {
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(value.getName()));
        cells.add(new CellData(value.getValue()));
        return new RowData(cells);
    }
}
