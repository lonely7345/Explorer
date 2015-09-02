package com.stratio.notebook.notebook.cassandra.operations;


import com.stratio.notebook.interpreter.InterpreterDriver;

public class OperationFactory {

    private InterpreterDriver driver;

    public OperationFactory(InterpreterDriver driver){
        this.driver = driver;
    }


    public CQLOperation operation(String operation){

        CQLOperation cqlOperation = null;
        if (operation.toUpperCase().equals("INSERT")) {
            cqlOperation = new InsertOperation(driver);
        }else {
            cqlOperation = new UpdateOperation(driver);
        }
        return cqlOperation;
    }

}
