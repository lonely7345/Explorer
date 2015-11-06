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
public class DescribeKeySpaceAnyExecutorTest {


    @Test
    public void whenDescribeKeySpace(){

        String keySpaceName = "demo";
        String createDemoScript ="CREATE KEYSPACE ";
        String creationFirstTable ="CREATE FIRST_TABLE";
        String creationSecond = "CREATE_SECOND_TABLE";
        DescribeExecutor executor = new DescribeKeySpaceAnyExecutor();
        Session mockSession = new CassandraSessionMocks().mockDescribe_keySpace_any(keySpaceName,createDemoScript,creationFirstTable,creationSecond);
        executor.optionalParam("demo");
        Table table = executor.execute(mockSession.getCluster().getMetadata());
        assertThat(table.header(),is(ListUtils.buildList(DescribeKeySpaceAnyExecutor.CT_SCRIPT)));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(createDemoScript));
        assertThat(table.rows().get(1).cells().get(0).getValue().toString(),is(creationFirstTable));
        assertThat(table.rows().get(2).cells().get(0).getValue().toString(),is(creationSecond));

    }
}
