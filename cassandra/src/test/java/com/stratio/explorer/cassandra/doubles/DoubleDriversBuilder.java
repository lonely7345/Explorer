package com.stratio.explorer.cassandra.doubles;

import com.stratio.explorer.cassandra.gateways.CassandraInterpreterGateways;

import com.stratio.explorer.cassandra.models.Table;



//TODO : when pass test then refactor
public class DoubleDriversBuilder {

    private boolean connectionIsEnabled=true;
    private boolean correctSyntax = true;

    public void driverWithConnectionShutDown(Table dataTable){
        CassandraInterpreterGateways.commandDriver  =  new DoubleCassandraDriver(new DoubleSessionCassandra(!connectionIsEnabled,false),correctSyntax,dataTable);
    }

    public void driverWithSyntaxError(Table dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(new DoubleSessionCassandra(connectionIsEnabled,false),!correctSyntax,dataTable);
    }

    public void driverWithCorrectCQL(Table dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(new DoubleSessionCassandra(connectionIsEnabled,false),correctSyntax,dataTable);
    }

    public void driverWithNotPropertFoundException(Table dataTable){
        boolean connectionIsEnabled=true;
        CassandraInterpreterGateways.commandDriver  = new DoubleCassandraDriver(new DoubleSessionCassandra(connectionIsEnabled,true),correctSyntax,dataTable);
    }





}

