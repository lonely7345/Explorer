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

import com.stratio.explorer.cassandra.functions.CassandraPropertyToInetSocket;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.FunctionalList;

import java.net.InetSocketAddress;
import java.util.*;


public class PropertiesReader {



    /**
     * Read properties to Cassandra Database
     * @param properties properties with (anyValue)->host:port estructure
     * @return Collection of InetSocketAddress
     */
    public Collection<InetSocketAddress> getListSocketAddres(Properties properties) {
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        FunctionalList<String,InetSocketAddress> functionalList = new FunctionalList<String,InetSocketAddress>(keys);
        return functionalList.map(new CassandraPropertyToInetSocket(properties));
    }
}
