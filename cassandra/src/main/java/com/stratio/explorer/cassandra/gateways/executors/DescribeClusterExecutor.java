package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.StringToCellDatafunction;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;

/**
 * Excute DESCRIBE CLUSTER shCQL
 */
public class DescribeClusterExecutor implements DescribeExecutor{

    public static final String CT_CLUSTER ="Cluster ";
    public static final String CT_PARTIRIONER ="Partitioner ";

    private Metadata metaData;


    public DescribeClusterExecutor(Metadata metaData){
        this.metaData = metaData;
    }

    /**
     * Excute DESCRIBE CLUSTER
     * @param params attributtes describe
     * @return
     */
    @Override
    public Table execute(String... params) {
        List<String> result = new ArrayList<>();

        return buildTable(metaData.getClusterName(),metaData.getPartitioner());
    }


    private Table buildTable(String clusterName,String partitioner){


        FunctionalList<String,CellData> functionalList = new FunctionalList<String,CellData>(buildList( clusterName, partitioner));
        List<CellData> cells = functionalList.map(new StringToCellDatafunction());
        RowData rowData = new RowData(cells);
        List<RowData> data = new ArrayList<>();
        data.add(rowData);
        return new Table(buildList(CT_CLUSTER, CT_PARTIRIONER),data);
    }


    private List<String> buildList(String... headers){
        List<String> result = new ArrayList<>();
        for (String cad:headers){
            result.add(cad);
        }
        return result;
    }
}
