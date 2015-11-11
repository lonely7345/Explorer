package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.NameValueToRowData;
import com.stratio.explorer.cassandra.models.NameValue;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.List;

/**
 * Excute DESCRIBE CLUSTER shCQL
 */
public class DescribeClusterExecutor implements DescribeExecutor{

    public static final String CT_CLUSTER ="Cluster";
    public static final String CT_PARTIRIONER ="Partitioner";
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
        FunctionalList<NameValue,RowData> functionalList =
                new FunctionalList<NameValue,RowData>(ListUtils.buildListNameValue(new NameValue(CT_CLUSTER, metaData.getClusterName()),
                                                                                   new NameValue(CT_PARTIRIONER, metaData.getPartitioner())
                                                      )
                                                   );
        List<RowData> data = functionalList.map(new NameValueToRowData());
        return new Table(ListUtils.buildList(),data);
    }





}
