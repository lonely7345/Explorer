package com.stratio.explorer.cassandra.gateways.executors;


import com.stratio.explorer.cassandra.models.Table;

import java.util.List;

/**
 * Execute describe in cassandra
 */

//TODO : THIS INTERFAZE NAME WILL BE CHANGED
public interface DescribeExecutor {


    /**
     * Execute Describe shCQL
     * @param params attributtes describe
     * @return  table with results
     */
    public Table execute(String... params);
}
