package com.stratio.notebook.cassandra.doubles;

import com.datastax.driver.core.ResultSet;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver<DataTable>{


    private boolean isUpService;
    private boolean correctSyntax;
    private DataTable initialDataInStore;

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax,DataTable initialDataInStore){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
        this.initialDataInStore = initialDataInStore;
    }

    @Override public void connect() {
        if (!isUpService)
            throw new ConnectionException("exception");
    }


    @Override public DataTable executeCommand(String command) {
        if (!correctSyntax)
           throw new CassandraInterpreterException("exception");
        return initialDataInStore;
    }
}
