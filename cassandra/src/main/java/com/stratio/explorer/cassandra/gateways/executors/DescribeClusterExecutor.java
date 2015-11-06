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
    public static final String WORD_SELECTOR ="CLUSTER";

    private Metadata metaData;


    /**
     * Excute DESCRIBE CLUSTER
     * @param params attributtes describe
     * @return
     */
    @Override
    public Table execute(Metadata metaData,String param) {
        FunctionalList<String,CellData> functionalList =
                new FunctionalList<String,CellData>(buildList( metaData.getClusterName(),
                                                               metaData.getPartitioner()
                                                              )
                                                   );
        List<RowData> data = new ArrayList<>();
        data.add(new RowData(functionalList.map(new StringToCellDatafunction())));
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
