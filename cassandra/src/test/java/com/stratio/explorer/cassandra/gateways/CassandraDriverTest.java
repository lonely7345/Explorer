/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
