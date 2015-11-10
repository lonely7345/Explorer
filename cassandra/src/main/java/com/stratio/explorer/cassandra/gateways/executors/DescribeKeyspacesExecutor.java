package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.KeyspacestoRowDataFuntion;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;


/**
 * Execute Describe Keysapces
 */
public class DescribeKeyspacesExecutor implements DescribeExecutor{

    private Metadata metaData;

    public static final String CT_KEYSPACES ="keySpaces";
    public static final String WORD_SELECTOR="KEYSPACES";


    /**
     * not mandoatory param .
     * @param param
     */
    public void optionalParam(String param){
       //left empty deliveraly
    }

    /**
     * Execute Describe Keysapces
     * @param metaData
     * @return  table
     */
    @Override
    public Table execute(Metadata metaData) {
        FunctionalList<KeyspaceMetadata,RowData> functional = new FunctionalList<>(metaData.getKeyspaces());
        List<RowData> rowDatas = functional.map(new KeyspacestoRowDataFuntion());
        return new Table(ListUtils.buildList(), rowDatas);
    }
}
