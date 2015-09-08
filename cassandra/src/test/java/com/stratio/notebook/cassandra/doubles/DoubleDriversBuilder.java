package com.stratio.notebook.cassandra.doubles;

import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.notebook.cassandra.models.DataTable;


public class DoubleDriversBuilder {

    private boolean connectionIsEnabled=true;
    private boolean correctSyntax = true;

    public void driverWithConnectionShutDown(DataTable dataTable){
        CassandraInterpreterGateways.commandDriver  =  new DoubleCassandraDriver(!connectionIsEnabled,correctSyntax,dataTable);
    }

    public void driverWithSyntaxError(DataTable dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(connectionIsEnabled,!correctSyntax,dataTable);
    }

    public void driverWithCorrectCQL(DataTable dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(connectionIsEnabled,correctSyntax,dataTable);
    }
}
