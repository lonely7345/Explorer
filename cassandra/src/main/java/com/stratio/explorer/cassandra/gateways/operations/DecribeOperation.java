package com.stratio.explorer.cassandra.gateways.operations;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.gateways.executors.*;
import com.stratio.explorer.cassandra.models.SHCQLOperation;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Execute Describe operations
 */
public class DecribeOperation implements CassandraOperation {



    /**
     * execute shCQLcoomand
     * @param Session session
     * @param shCQLcommand command shCQL
     * @return List string with result
     */
    @Override
    public Table execute(Session session, String shCQLcommand) {

        SHCQLOperation shCqlOperation = new SHCQLOperation(shCQLcommand);
        Cluster cluster = session.getCluster();
        DescribeExecutor executor = DescribeExecutorFactory.select(shCqlOperation.identifier());
        executor.optionalParam(shCqlOperation.optionalValue());
        return executor.execute(cluster.getMetadata());
    }
}
