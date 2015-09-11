package com.stratio.notebook.cassandra.doubles;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.gateways.CassandraDriver;
import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.notebook.cassandra.models.RowData;
import com.stratio.notebook.cassandra.models.Table;

import java.util.Properties;


public class DoubleDriversBuilder {

    private boolean connectionIsEnabled=true;
    private boolean correctSyntax = true;

    public void driverWithConnectionShutDown(Table dataTable){
        CassandraInterpreterGateways.commandDriver  =  new DoubleCassandraDriver(!connectionIsEnabled,correctSyntax,dataTable);
    }

    public void driverWithSyntaxError(Table dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(connectionIsEnabled,!correctSyntax,dataTable);
    }

    public void driverWithCorrectCQL(Table dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(connectionIsEnabled,correctSyntax,dataTable);
    }

}