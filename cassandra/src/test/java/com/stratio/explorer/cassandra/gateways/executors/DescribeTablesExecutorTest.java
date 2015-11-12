/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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

package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.testng.annotations.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;


public class DescribeTablesExecutorTest {



    @Test
    public void whenDescribeTables(){
        String keySpaceName="keySpaceName";
        String nameTableOne="nameTableOne";
        String nameTableTwo="nameTableTwo";
        DescribeTablesExecutor executor = new DescribeTablesExecutor();
        Session session = new CassandraSessionMocks().mockDescribeTables(keySpaceName, nameTableOne, nameTableTwo);
        Table table =  executor.execute(session.getCluster().getMetadata());
        assertThat("Header should be empty",table.header(),is(new ListUtils<String>().buildList()));


        Table tableCell = (Table)table.rows().get(0).cells().get(0).getValue();
        assertThat(table.rows().get(0).cells().get(0).getValue(),instanceOf(Table.class));

        assertThat(tableCell.header(),is(new ListUtils<String>().buildList("Keyspace "+keySpaceName)));
        assertThat((String)tableCell.rows().get(0).cells().get(0).getValue(),is(nameTableOne));
        assertThat((String)tableCell.rows().get(0).cells().get(1).getValue(),is(nameTableTwo));

    }
}
