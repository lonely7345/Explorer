package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.functions.TransformFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform TableMetadata to CellData
 */
public class TableMetaDataToCellData implements TransformFunction<TableMetadata,CellData> {

    /**
     *  Transform TableMetadata to CellData with TableName Value
     * @param value
     * @return CellData with Table value
     */
    @Override
    public CellData transform(TableMetadata value) {

        return new CellData(value.getName());
    }

    private Table buildTable(TableMetadata value){
        List<String> header = ListUtils.buildList(value.getKeyspace().getName());
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(value.getName()));
        List<RowData> rows = new ArrayList<>();
        rows.add(new RowData(cells));
        return  new Table(header,rows);
    }

}