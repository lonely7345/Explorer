package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.models.Table;
import java.util.List;

/**
 * Execute describe in cassandra
 */

//TODO : THIS INTERFAZ WILL BE CHANGED
public interface DescribeExecutor {


    /**
     * Execute Describe shCQL
     * @param params attributtes describe
     * @return  table with results
     */
    public Table execute(Metadata metaData,String params);
}
