package com.stratio.explorer.cassandra.gateways;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.doubles.DoubleSession;
import com.stratio.explorer.cassandra.doubles.MockSessionCassandra;
import com.stratio.explorer.cassandra.models.CellData;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CassandraDriverTest  {


    private CassandraDriver driver;



    @Before
    public void setUp() throws InterruptedException, IOException {
        driver = new CassandraDriver(new MockSessionCassandra(new DoubleSession().mockSession()));
    }

    @Test
    public void headerWillBeRecovered() throws InterruptedException, IOException {

        Table result = driver.executeCommand("select * from mytable WHERE id='myKey01'");
        List<String> header = new ArrayList<>();
        header.add("");
        assertThat(result.header(), is(header));
    }


    @Test
    public void rowsWillBerecovered(){

        Table result = driver.executeCommand("select * from mytable WHERE id='myKey01'");
        assertThat(result.rows().size(), is(1));
    }


    @Test
    public void cellsWitllBeRecovered(){
        Table result = driver.executeCommand("select * from mytable WHERE id='myKey01'");
        RowData rows = result.rows().get(0);
        List<CellData> cells = rows.cells();
        assertThat(cells.size(), is(1));
    }
}
