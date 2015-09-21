package com.stratio.notebook.cassandra.operations;

import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;


import com.stratio.notebook.cassandra.models.Table;


import com.stratio.notebook.interpreter.InterpreterDriver;

public class CQLExecutor {


    private InterpreterDriver<Table> driver = CassandraInterpreterGateways.commandDriver;

    public Table execute(String command) {
         driver.connect();
         return driver.executeCommand(command);
    }
}
