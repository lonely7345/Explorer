package com.stratio.notebook.cassandra.doubles;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver{


    private boolean isUpService;
    private boolean correctSyntax;

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
    }

    @Override public void connect() {
        if (!isUpService)
            throw new ConnectionException("exception");
    }


    @Override public ResultSet executeCommand(String command) {
        if (!correctSyntax)
           throw new CassandraInterpreterException("exception");
        return null;
    }
}
