package com.stratio.explorer.cassandra.gateways;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.gateways.executors.DescribeClusterExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeySpaceAnyExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeyspacesExecutor;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Execute Describe KeySpace
 */
//TODO : CHANGE NAME
public class CassandraDriverWithDescribe {


    /**
     * execute shCQLcoomand
     * @param shCQLcommand
     * @return List string with result
     */
    public Table execute(Session session,String shCQLcommand) {
        String operation = extracOperation(shCQLcommand);
        Cluster cluster = session.getCluster();
        Metadata metaData = cluster.getMetadata();
        if (operation.toUpperCase().equals("CLUSTER")){
           return new DescribeClusterExecutor(metaData).execute();
        }else if (operation.toUpperCase().equals("KEYSPACES")) {
            return new DescribeKeyspacesExecutor(metaData).execute();
        }else if (operation.toUpperCase().equals("KEYSPACE")){
            return new DescribeKeySpaceAnyExecutor(metaData).execute(shCQLcommand.split(" +")[2]);
        }
        return null;
    }


    private String extracOperation(String shCQLcommand){
        String [] operation = shCQLcommand.split(" +");
        return operation[1];
    }
}
