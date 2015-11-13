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

package com.stratio.explorer.cassandra.gateways.executors;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DescribeClusterExecutorTest {



    @Test
    public void whenDescribeCluster(){
        String clusterName = "clusetrName";
        String partitioner = "partitioner";
        DescribeExecutor executor = new DescribeClusterExecutor();
        Session mockSession = new CassandraSessionMocks().mockDescribeCluster(clusterName, partitioner);
        Table table = executor.execute(mockSession.getCluster().getMetadata());

        assertThat("Header should be empty",table.header(), is(new ListUtils<String>().buildList()));
        assertThat("should be Identifier ",table.rows().get(0).cells().get(0).getValue().toString(), is(DescribeClusterExecutor.CT_CLUSTER));
        assertThat("should be Identifier ",table.rows().get(1).cells().get(0).getValue().toString(),is(DescribeClusterExecutor.CT_PARTIRIONER));

        assertThat("should be value ",table.rows().get(0).cells().get(1).getValue().toString(),is(clusterName));
        assertThat("should be value ",table.rows().get(1).cells().get(1).getValue().toString(),is(partitioner));
    }


}
