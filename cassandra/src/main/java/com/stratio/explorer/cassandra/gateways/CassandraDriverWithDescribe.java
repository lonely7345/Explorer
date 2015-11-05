package com.stratio.explorer.cassandra.gateways;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.gateways.executors.DescribeClusterExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeyspaceExecutor;
import com.stratio.explorer.cassandra.models.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Execute Describe KeySpace
 */
public class CassandraDriverWithDescribe {


    private CassandraSession cassandraSession;




    /**
     * Class contructor
     * @param cassandraSession
     */
    public CassandraDriverWithDescribe(CassandraSession cassandraSession) {
        this.cassandraSession = cassandraSession;
    }


    /**
     * execute shCQLcoomand
     * @param shCQLcommand
     * @return List string with result
     */
    public Table execute(String shCQLcommand) {
        String operation = extracOperation(shCQLcommand);
        Session session = cassandraSession.getConnector();
        Cluster cluster = session.getCluster();
        Metadata metaData = cluster.getMetadata();
        if (operation.toUpperCase().equals("CLUSTER")){
           return new DescribeClusterExecutor(metaData).execute();
        }else if (operation.toUpperCase().equals("KEYSPACES")) {
            return new DescribeKeyspaceExecutor(metaData).execute();
        }
        return null;
    }


    private String extracOperation(String shCQLcommand){
        String [] operation = shCQLcommand.split(" +");
        return operation[1];
    }
}
