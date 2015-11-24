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

public class CassandraDriverTest  {


    private CassandraDriver driver;


    @Before
    public void setUp() throws InterruptedException, IOException {
        driver = new CassandraDriver(new MockSessionCassandra(new DoubleSession().mockSessionWithData()));
    }


    @Test
    public void whenOperationIsNormalCQL() throws InterruptedException, IOException {
        Table result = driver.executeCommand(DoubleSession.CT_SELECT);
        List<String> header = new ArrayList<>();
        header.add("");
        assertThat("Header sould be equals a header table",result.header(), is(header));
    }


    @Test
    public void whenOperationIsSHCQL() {
        String nameKeySpace = "nameKeySpace";
        driver = new CassandraDriver(new MockSessionCassandra(new CassandraSessionMocks().mockDescribeKeySpaces(nameKeySpace)));
        Table result = driver.executeCommand("DESCRIBE keySpaces");
        assertThat(result.header(), is(new ListUtils<String>().buildList()));
        assertThat(result.rows().get(0).cells().get(0).getValue().toString(), is(nameKeySpace));
    }

}
