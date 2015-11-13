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


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.explorer.cassandra.models.Table;
import org.junit.Test;

import com.stratio.explorer.reader.PropertiesReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by afidalgo on 4/11/15.
 */
public class CassandraRealDriverTest {





   /*@Test
    public void whenUseDescribe(){
        CassandraSession sesion = new CassandraSession();
        sesion.loadConfiguration(new PropertiesReader().readConfigFrom("cassandra"));

        CassandraDriver driver = new CassandraDriver(sesion);
       Table table =driver.executeCommand("USE DEMO");
       Table table1 = driver.executeCommand("SELECT * FROM DEPARTMENT");
       int a =0;
       int b=1;
    //    mockDescribeKeySpacedemo(sesion.getConnector().getCluster());
    //   mockDescribeTables(sesion.getConnector().getCluster());
    //    mockDescribeTableDemo(sesion.getConnector().getCluster());

    }*/


    private void mockDescribeKeySpacedemo(Cluster cluster){
        Metadata metaData = cluster.getMetadata();
        KeyspaceMetadata metaDataDemo = metaData.getKeyspace("demo");

        String createDemo = metaDataDemo.toString();

        List<TableMetadata> list =  new ArrayList<TableMetadata>( metaDataDemo.getTables());

        TableMetadata second = list.get(0);
        String secondName = second.toString();



        TableMetadata third = list.get(1);
        String thirdName = third.toString();

        TableMetadata fourth = list.get(2);
        String fourthName = fourth.toString();
        int c =0;

    }



    private void mockDescribeTables(Cluster cluster){
        Metadata metaData = cluster.getMetadata();

        List<KeyspaceMetadata> list = metaData.getKeyspaces();


        /********** Keyspace demo ****/
        KeyspaceMetadata demoMetaData = list.get(0);
        String nameKeySpace = demoMetaData.getName();
        List<TableMetadata> tables = new ArrayList<TableMetadata> (demoMetaData.getTables());

        TableMetadata tableOne = tables.get(0);
        String tableOneName = tableOne.getName();

        TableMetadata tableTwo = tables.get(1);
        String tableTwoName = tableTwo.getName();


        int a =0;
    }


    private void mockDescribeTableDemo(Cluster cluster){
        Metadata metaData = cluster.getMetadata();
        KeyspaceMetadata metaDataDemo = metaData.getKeyspace("demo");

        TableMetadata table = metaDataDemo.getTable("users");

        String data = table.toString();

        int a = 0;
    }
}
