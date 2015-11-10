package com.stratio.explorer.cassandra.gateways.operations;


import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.doubles.DoubleSession;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CQLOperationsTest {


    private CQLOperations cqlOperation;
    private DoubleSession doubleSession;


    @Before
    public void setUp() throws InterruptedException, IOException {
        cqlOperation = new CQLOperations();
        doubleSession = new DoubleSession();

    }

    @Test
    public void headerWillBeRecovered() throws InterruptedException, IOException {

        Table result = cqlOperation.execute(doubleSession.mockSessionWithData(), "select * from mytable WHERE id='myKey01'");
        List<String> header = new ArrayList<>();
        header.add("");
        assertThat("Header sould be equals a header table",result.header(), is(header));
    }


    @Test
    public void rowsWillBerecovered(){

        Table result = cqlOperation.execute(doubleSession.mockSessionWithData(), "select * from mytable WHERE id='myKey01'");
        assertThat("rows should be equals to result in table",result.rows().size(), is(1));
    }


    @Test
    public void cellsWillBeRecovered(){
        Table result = cqlOperation.execute(doubleSession.mockSessionWithData(),"select * from mytable WHERE id='myKey01'");
        RowData rows = result.rows().get(0);
        List<CellData> cells = rows.cells();
        assertThat("Cells should be equals number cells in row",cells.size(), is(1));
    }


    @Test
    public void whenExecuteCQLThatNotReturnData(){
        Table result = cqlOperation.execute(new DoubleSession().mockSessionWithOutData(),"USE DEMO");
        RowData rows = result.rows().get(0);
        List<CellData> cells = rows.cells();
        assertThat("if not returned values then result is successfull ",cells.size(),is(1));
        assertThat("if not returned values then result is successfull ",cells.get(0).getValue().toString(),is(StringConstants.OPERATION_OK));
    }
}
