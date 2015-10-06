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

import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.models.Table;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver<Table>{


    private boolean isUpService;
    private boolean correctSyntax;
    private Table initialDataInStore;

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax,Table initialDataInStore){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
        this.initialDataInStore = initialDataInStore;
    }

    @Override public void connect() {
        if (!isUpService)
            throw new ConnectionException(new Exception(),"exception");
    }


    @Override public Table executeCommand(String command) {
        if (!correctSyntax)
            throw new CassandraInterpreterException(new Exception(),"exception");
        return initialDataInStore;
    }
}