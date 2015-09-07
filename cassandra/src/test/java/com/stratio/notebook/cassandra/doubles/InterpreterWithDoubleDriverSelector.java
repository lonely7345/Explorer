package com.stratio.notebook.cassandra.doubles;

import com.stratio.notebook.cassandra.CassandraInterpreter;
import com.stratio.notebook.interpreter.Interpreter;

//TENGO QUE VER LO QUE QUIERO QUE ME DEVUELVA EL INSERT
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
