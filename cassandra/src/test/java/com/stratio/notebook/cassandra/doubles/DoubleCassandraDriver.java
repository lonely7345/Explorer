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
            throw new ConnectionException("exception");
    }


    @Override public Table executeCommand(String command) {
        if (!correctSyntax)
            throw new CassandraInterpreterException("exception");
        return initialDataInStore;
    }
}