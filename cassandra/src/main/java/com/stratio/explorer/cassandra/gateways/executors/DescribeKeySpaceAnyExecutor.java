package com.stratio.explorer.cassandra.gateways.executors;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.functions.TableMetadataToRowDataFunction;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Execute DECRIBE KEYSPACE any
 */
public class DescribeKeySpaceAnyExecutor implements DescribeExecutor{



    public static final String WORD_SELECTOR="KEYSPACE";
    private String param;


    /**
     * Last param of describe keyspaces
     * @param param
     */
    public void optionalParam(String param){
        this.param = param;
    }

    /**
     *  Execute when shCQL is DESCRIBE KEYSPACE anyvalue
     *  @param metaData
     * @return  table
     */
    @Override
    public Table execute(Metadata metaData) {
        KeyspaceMetadata keySpaceMetada = metaData.getKeyspace(param);
        FunctionalList<TableMetadata,RowData> functionalList = new FunctionalList<>(new ArrayList<>(keySpaceMetada.getTables()));
        List<RowData> rows = functionalList.map(new TableMetadataToRowDataFunction());
        rows.add(0,buildFirst(keySpaceMetada.toString()));
        return new Table(ListUtils.buildList(),rows);
    }

    private RowData buildFirst(String valueuniqueCell){
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(valueuniqueCell));
        return new RowData(cells);
    }


}
