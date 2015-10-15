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

package com.stratio.notebook.cassandra.doubles;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class DoubleSession {


    public Session buildSession(){
        Cluster cluster = new Cluster.Builder().addContactPoints("localhost").withPort(9042).build();
        Session session = cluster.connect();
        session.execute("CREATE KEYSPACE ddf WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1}");
        session.execute("USE ddf");
        session.execute("CREATE TABLE myTable(id varchar, value varchar, PRIMARY KEY(id));");
        session.execute("INSERT INTO myTable(id, value) values('myKey01','myValue01');");
        return session;
    }
}
