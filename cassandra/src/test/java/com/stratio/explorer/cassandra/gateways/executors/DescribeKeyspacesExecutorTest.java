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
        assertThat("header should be empty",table.header(), is(new ListUtils<String>().buildList()));
        assertThat("name key Space",table.rows().get(0).cells().get(0).getValue().toString(),is(nameKeySpace));
    }

}
