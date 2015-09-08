package com.stratio.notebook.cassandra.operations;

import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.interpreter.InterpreterDriver;

public class CQLExecutor {

    private InterpreterDriver<DataTable> driver = CassandraInterpreterGateways.commandDriver;

    public DataTable execute(String command) {
         driver.connect();
         return driver.executeCommand(command);
    }
}
