package com.stratio.explorer.cassandra.gateways;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.gateways.executors.*;
import com.stratio.explorer.cassandra.models.SHCQLOperation;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Execute Describe KeySpace
 */
public class CassandraDriverWithDescribe {



    /**
     * execute shCQLcoomand
     * @param shCQLcommand
     * @return List string with result
     */
    public Table execute(Session session,String shCQLcommand) {

        SHCQLOperation shCqlOperation = new SHCQLOperation(shCQLcommand);
        Cluster cluster = session.getCluster();
        DescribeExecutor executor = DescribeExecutorFactory.select(shCqlOperation.identifier());
        return executor.execute(cluster.getMetadata(),shCqlOperation.optionalValue());
    }
}
