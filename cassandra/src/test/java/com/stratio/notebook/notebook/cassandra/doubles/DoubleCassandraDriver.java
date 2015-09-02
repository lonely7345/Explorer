package com.stratio.notebook.notebook.cassandra.doubles;

import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver{


    private boolean isUpService;
    private boolean correctSyntax;

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
    }

    @Override public boolean isUpService() {
        return isUpService;
    }

    @Override public void executeCommand(String command) {
        if (!correctSyntax)
           throw new InvalidQueryException("");
    }
}
