package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.models.Table;
import java.util.List;

/**
 * Execute describe in cassandra
 */
public interface DescribeExecutor {


    /**
     * last param in Describe with three params
     * @param param
     */
    public void optionalParam(String param);

    /**
     * Execute Describe shCQL
     * @param metaData
     * @return  table with results
     */
    public Table execute(Metadata metaData);
}
