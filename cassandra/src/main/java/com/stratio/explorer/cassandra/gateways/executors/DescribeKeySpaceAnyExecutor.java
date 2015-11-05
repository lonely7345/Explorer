package com.stratio.explorer.cassandra.gateways.executors;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.functions.TableMetadataToRowDataFunction;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.lists.FunctionalList;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import javafx.scene.control.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Execute DECRIBE KEYSPACE any
 */
public class DescribeKeySpaceAnyExecutor implements DescribeExecutor{


    public static final String CT_SCRIPT ="SCRIPT IMPLEMNETATION";
    private Metadata metaData;

    /**
     * Constructor
     * @param metaData
     */
    public DescribeKeySpaceAnyExecutor(Metadata metaData){
         this.metaData = metaData;
    }


    /**
     *  Execute when shCQL is DESCRIBE KEYSPACE anyvalue
     * @param params attributtes describe
     * @return
     */
    @Override
    public Table execute(String... params) {
        KeyspaceMetadata keySpaceMetada = metaData.getKeyspace(params[0]);
        FunctionalList<TableMetadata,RowData> functionalList = new FunctionalList<>(new ArrayList<>(keySpaceMetada.getTables()));
        List<RowData> rows = functionalList.map(new TableMetadataToRowDataFunction());
        rows.add(0,buildFirst(keySpaceMetada.toString()));
        return new Table(buildHeader(CT_SCRIPT),rows);
    }

    private RowData buildFirst(String valueuniqueCell){
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(valueuniqueCell));
        return new RowData(cells);
    }


    private List<String> buildHeader(String... headers){
        List<String> result = new ArrayList<>();
        for (String cad:headers){
            result.add(cad);
        }
        return result;
    }
}
