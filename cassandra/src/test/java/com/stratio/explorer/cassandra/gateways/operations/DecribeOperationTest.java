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


import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.gateways.executors.DescribeClusterExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeySpaceAnyExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeyspacesExecutor;
import com.stratio.explorer.cassandra.models.Table;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class DecribeOperationTest {


    private DecribeOperation describe;


    @Before
    public void setUp(){
        describe = new DecribeOperation();
    }

    @Test
    public void whenDescribeCluster(){
        String clusterName = "clusetrName";
        String partitioner = "partitioner";
        Session mockSession = new CassandraSessionMocks().mockDescribeCluster(clusterName, partitioner);
        Table table = describe.execute(mockSession,"DESCRIBE CLUSTER");
        assertThat("header should be empty",table.header(), is(buildList()));
        assertThat("return cluster",table.rows().get(0).cells().get(0).getValue().toString(),is(DescribeClusterExecutor.CT_CLUSTER));
        assertThat("return cluster",table.rows().get(1).cells().get(0).getValue().toString(), is(DescribeClusterExecutor.CT_PARTIRIONER));

        assertThat("return cluster",table.rows().get(0).cells().get(1).getValue().toString(),is(clusterName));
        assertThat("return cluster",table.rows().get(1).cells().get(1).getValue().toString(),is(partitioner));
    }

    @Test
    public void whenDescribeKeySpaces(){
        String nameKeySpace ="unicquename";
        Session mockSession = new CassandraSessionMocks().mockDescribeKeySpaces(nameKeySpace);
        Table table = describe.execute(mockSession,"DESCRIBE KEYSPACES");
        assertThat("return keySpaces",table.header(), is(buildList()));
        assertThat("return keySpaces",table.rows().get(0).cells().get(0).getValue().toString(),is(nameKeySpace));
    }

    @Test
    public void whenDescribeKeySpace(){

        String keySpaceName = "demo";
        String createDemoScript ="CREATE KEYSPACE ";
        String creationFirstTable ="CREATE FIRST_TABLE";
        String creationSecond = "CREATE_SECOND_TABLE";
        Session mockSession = new CassandraSessionMocks().mockDescribe_keySpace_any(keySpaceName,createDemoScript,creationFirstTable,creationSecond);
        Table table = describe.execute(mockSession, "DESCRIBE KEYSPACE demo");
        assertThat("return keySpace",table.header(),is(buildList()));
        assertThat("return keySpace",table.rows().get(0).cells().get(0).getValue().toString(), is(createDemoScript));
        assertThat("return keySpace",table.rows().get(1).cells().get(0).getValue().toString(),is(creationFirstTable));
        assertThat("return keySpace",table.rows().get(2).cells().get(0).getValue().toString(),is(creationSecond));

    }


    private List<String> buildList(String... cadResults){
       List<String> result = new ArrayList<>();
       for (String cad:cadResults){
           result.add(cad);
       }
       return result;
    }
}
