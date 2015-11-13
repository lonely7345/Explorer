/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
