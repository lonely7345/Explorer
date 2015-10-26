package com.stratio.explorer.cassandra.doubles;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.interpreter.InterpreterDriver;

public class DoubleCassandraDriver implements InterpreterDriver<Table>{


    private Connector<Session> connector;
    private boolean correctSyntax;
    private Table initialDataInStore;

    public DoubleCassandraDriver(Connector<Session> connector,boolean correctSyntax,Table initialDataInStore){

        this.connector = connector;
        this.correctSyntax = correctSyntax;
        this.initialDataInStore = initialDataInStore;
    }

    @Override public Table executeCommand(String command) {
        connector.getConnector();
        if (!correctSyntax)
            throw new CassandraInterpreterException(new Exception(),"exception");
        return initialDataInStore;
    }

    @Override
    public Connector getConnector() {
        return connector;
    }
}