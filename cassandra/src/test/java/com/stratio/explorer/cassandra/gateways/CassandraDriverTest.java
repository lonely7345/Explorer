package com.stratio.explorer.cassandra.gateways;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.gateways.executors.DescribeExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeyspacesExecutor;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.doubles.DoubleSession;
import com.stratio.explorer.cassandra.doubles.MockSessionCassandra;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO : THIS TEST WILL BE CHANGED
public class CassandraDriverTest  {


    private CassandraDriver driver;


    @Before
    public void setUp() throws InterruptedException, IOException {
        driver = new CassandraDriver(new MockSessionCassandra(new DoubleSession().mockSessionWithData()));
    }

    @Test
    public void whenOperationIsNormalCQL() throws InterruptedException, IOException {

        Table result = driver.executeCommand("select * from mytable WHERE id='myKey01'");
        List<String> header = new ArrayList<>();
        header.add("");
        assertThat("Header sould be equals a header table",result.header(), is(header));
    }


  /*  @Test
    public void whenOperationIsSHCQL(){
        String nameKeySpace = "nameKeySpace";
        driver = new CassandraDriver(new MockSessionCassandra(new CassandraSessionMocks().mockDescribeKeySpaces(nameKeySpace)));
        Table result = driver.executeCommand("DESCRIBE keySpaces");
        assertThat(result.header(), is(ListUtils.buildList()));
        assertThat(result.rows().get(0).cells().get(0).getValue().toString(),is(nameKeySpace));
    }*/

}
