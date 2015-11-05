package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.functions.KeyspacestoRowDataFuntion;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.List;


/**
 * Execute Describe Keysapces
 */
public class DescribeKeyspacesExecutor implements DescribeExecutor{

    private Metadata metaData;

    public static final String CT_KEYSPACES ="keySpaces";


    public DescribeKeyspacesExecutor(Metadata metaData){
        this.metaData = metaData;
    }



    /**
     * Execute Describe Keysapces
     * @param params attributtes describe
     * @return
     */
    @Override
    public Table execute(String... params) {
        FunctionalList<KeyspaceMetadata,RowData> functional = new FunctionalList<>(metaData.getKeyspaces());
        List<RowData> rowDatas = functional.map(new KeyspacestoRowDataFuntion());
        return new Table(buildHeader(CT_KEYSPACES),rowDatas);
    }

    private List<String> buildHeader(String... headers){
        List<String> result = new ArrayList<>();
        for (String cad:headers){
            result.add(cad);
        }
        return result;
    }
}
