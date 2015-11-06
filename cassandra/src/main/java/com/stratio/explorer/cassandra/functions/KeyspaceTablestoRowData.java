package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform KeySpacetables to RowData with tableName and KeyspaceName
 */
public class KeyspaceTablestoRowData implements TransformFunction<KeyspaceMetadata,RowData> {


    /**
     *
     * @param keyspaceMetadata
     * @return ransform KeySpacetables to CellData with tableName
     */
    @Override
    public RowData transform(KeyspaceMetadata keyspaceMetadata) {

        List<TableMetadata> tables = new ArrayList<>(keyspaceMetadata.getTables());
        FunctionalList<TableMetadata,CellData> functional = new FunctionalList<>(tables);
        List<CellData> tableNames = functional.map(new TableMetaDataToCellData());
        tableNames.add(0,new CellData(keyspaceMetadata.getName()));
        return new RowData(tableNames);
    }
}
