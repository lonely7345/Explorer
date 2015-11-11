package com.stratio.explorer.cassandra.gateways.executors;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class DescribeKeyspacesExecutorTest {

    @Test
    public void whenDescribeKeySpaces(){
        String nameKeySpace ="unicquename";
        Session mockSession = new CassandraSessionMocks().mockDescribeKeySpaces(nameKeySpace);
        DescribeExecutor executor = new DescribeKeyspacesExecutor();
        Table table = executor.execute(mockSession.getCluster().getMetadata());
        assertThat("header should be empty",table.header(), is(ListUtils.buildList()));
        assertThat("name key Space",table.rows().get(0).cells().get(0).getValue().toString(),is(nameKeySpace));
    }

}
