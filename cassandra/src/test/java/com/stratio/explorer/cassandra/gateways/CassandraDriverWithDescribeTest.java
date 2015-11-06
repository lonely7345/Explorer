package com.stratio.explorer.cassandra.gateways;


import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.gateways.executors.DescribeClusterExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeySpaceAnyExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeyspacesExecutor;
import com.stratio.explorer.cassandra.models.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class CassandraDriverWithDescribeTest {


    // DESCRIBE CLUSTER; (Inlude Table) (done)
    // DESCRIBE KEYSPACES; **
    // DESCRIBE KEYSPACE PortfolioDemo; **
    // DESCRIBE TABLES; ** (1 HORA)
    // DESCRIBE TABLE Stocks; (1 HORA)

    CassandraDriverWithDescribe describe;


    @Before
    public void setUp(){
        describe = new  CassandraDriverWithDescribe();
    }

    @Test
    public void whenDescribeCluster(){
        String clusterName = "clusetrName";
        String partitioner = "partitioner";
        Session mockSession = new CassandraSessionMocks().mockDescribeCluster(clusterName, partitioner);
        Table table = describe.execute(mockSession,"DESCRIBE CLUSTER");
        assertThat(table.header(), is(buildList(DescribeClusterExecutor.CT_CLUSTER, DescribeClusterExecutor.CT_PARTIRIONER)));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(clusterName));
        assertThat(table.rows().get(0).cells().get(1).getValue().toString(),is(partitioner));
    }

    @Test
    public void whenDescribeKeySpaces(){
        String nameKeySpace ="unicquename";
        Session mockSession = new CassandraSessionMocks().mockDescribeKeySpaces(nameKeySpace);
        Table table = describe.execute(mockSession,"DESCRIBE KEYSPACES");
        assertThat(table.header(), is(buildList(DescribeKeyspacesExecutor.CT_KEYSPACES)));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(nameKeySpace));
    }

    @Test
    public void whenDescribeKeySpace(){

        String keySpaceName = "demo";
        String createDemoScript ="CREATE KEYSPACE ";
        String creationFirstTable ="CREATE FIRST_TABLE";
        String creationSecond = "CREATE_SECOND_TABLE";
        Session mockSession = new CassandraSessionMocks().mockDescribe_keySpace_any(keySpaceName,createDemoScript,creationFirstTable,creationSecond);
        Table table = describe.execute(mockSession, "DESCRIBE KEYSPACE demo");
        assertThat(table.header(),is(buildList(DescribeKeySpaceAnyExecutor.CT_SCRIPT)));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(createDemoScript));
        assertThat(table.rows().get(1).cells().get(0).getValue().toString(),is(creationFirstTable));
        assertThat(table.rows().get(2).cells().get(0).getValue().toString(),is(creationSecond));

    }


    private List<String> buildList(String... cadResults){
       List<String> result = new ArrayList<>();
       for (String cad:cadResults){
           result.add(cad);
       }
       return result;
    }
}
