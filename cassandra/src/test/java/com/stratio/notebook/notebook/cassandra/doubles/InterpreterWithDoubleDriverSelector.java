package com.stratio.notebook.notebook.cassandra.doubles;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.notebook.cassandra.CassandraInterpreter;

public class InterpreterWithDoubleDriverSelector {

    private boolean connectionIsEnabled=true;
    private boolean correctSyntax = true;

    public Interpreter driverWithConnectionShutDown(){

          return new CassandraInterpreter(new DoubleCassandraDriver(!connectionIsEnabled,correctSyntax));
    }


    public Interpreter driverWithSyntaxError(){
        boolean connectionIsEnabled=true;
        return new CassandraInterpreter(new DoubleCassandraDriver(connectionIsEnabled,!correctSyntax));
    }

    public Interpreter driverWithCorrectInset(){
        boolean connectionIsEnabled=true;
        return new CassandraInterpreter(new DoubleCassandraDriver(connectionIsEnabled,correctSyntax));
    }
}
