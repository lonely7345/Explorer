package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.StringToCellData;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
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
     * In this class is not mandatory
     * @param param
     */
    public void optionalParam(String param){
        //left empty deliverely
    }


    /**
     * Excute DESCRIBE CLUSTER
     * @param params attributtes describe
     * @return
     */
    @Override
    public Table execute(Metadata metaData) {
        FunctionalList<String,CellData> functionalList =
                new FunctionalList<String,CellData>(ListUtils.buildList(metaData.getClusterName(),
                                                                        metaData.getPartitioner()
                                                                        )
                                                   );
        List<RowData> data = new ArrayList<>();
        data.add(new RowData(functionalList.map(new StringToCellData())));
        return new Table(ListUtils.buildList(CT_CLUSTER, CT_PARTIRIONER),data);
    }

}
