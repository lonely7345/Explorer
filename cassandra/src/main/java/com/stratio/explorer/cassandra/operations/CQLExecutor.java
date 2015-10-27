package com.stratio.explorer.cassandra.operations;

import com.stratio.explorer.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.explorer.cassandra.models.Table;


import com.stratio.explorer.interpreter.InterpreterDriver;

public class CQLExecutor {


    private InterpreterDriver<Table> driver = CassandraInterpreterGateways.commandDriver;

    public Table execute(String command) {
         return driver.executeCommand(command);
    }
}
