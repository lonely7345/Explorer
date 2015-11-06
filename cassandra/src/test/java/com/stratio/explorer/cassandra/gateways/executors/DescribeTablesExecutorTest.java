package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.models.Table;
import org.testng.annotations.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


public class DescribeTablesExecutorTest {



    @Test
    public void whenDescribeTables(){
        String keySpaceName="keySpaceName";
        String nameTableOne="nameTableOne";
        String nameTableTwo="nameTableTwo";
        DescribeTablesExecutor executor = new DescribeTablesExecutor();
        Session session = new CassandraSessionMocks().mockDescribeTables(keySpaceName,nameTableOne,nameTableTwo);
        Table table =  executor.execute(session.getCluster().getMetadata());
        assertThat("First column should be keySpace Name",table.header().get(0),is(DescribeTablesExecutor.NAME_KEYSPACE));
        assertThat("Second column should be Table Name",table.header().get(1),is(DescribeTablesExecutor.NAME_TABLE));
        assertThat("Second column should be Table Name",table.header().get(2),is(DescribeTablesExecutor.NAME_TABLE));

        assertThat("First column should be keySpace Name",table.rows().get(0).cells().get(0).getValue().toString(),is(keySpaceName));
        assertThat("Second column should be Table Name",table.rows().get(0).cells().get(1).getValue().toString(),is(nameTableOne));
        assertThat("Third column should be Table Name",table.rows().get(0).cells().get(2).getValue().toString(),is(nameTableTwo));

    }
}
