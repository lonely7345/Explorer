package com.stratio.explorer.cassandra.gateways.executors;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DescribeClusterExecutorTest {



    @Test
    public void whenDescribeCluster(){
        String clusterName = "clusetrName";
        String partitioner = "partitioner";
        DescribeExecutor executor = new DescribeClusterExecutor();
        Session mockSession = new CassandraSessionMocks().mockDescribeCluster(clusterName, partitioner);
        Table table = executor.execute(mockSession.getCluster().getMetadata());

        assertThat(table.header(), is(ListUtils.buildList()));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(DescribeClusterExecutor.CT_CLUSTER));
        assertThat(table.rows().get(1).cells().get(0).getValue().toString(),is(DescribeClusterExecutor.CT_PARTIRIONER));

        assertThat(table.rows().get(0).cells().get(1).getValue().toString(),is(clusterName));
        assertThat(table.rows().get(1).cells().get(1).getValue().toString(),is(partitioner));
    }


}
