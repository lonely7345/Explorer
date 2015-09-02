package com.stratio.notebook.notebook.cassandra.operations;


import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.stratio.notebook.interpreter.InterpreterDriver;
import com.stratio.notebook.notebook.cassandra.constants.MessageConstants;
import com.stratio.notebook.notebook.cassandra.exceptions.CassandraInterpreterException;

public class InsertOperation implements CQLOperation{


    private InterpreterDriver driver;

    public InsertOperation(InterpreterDriver driver){
        this.driver = driver;
    }

    @Override public String execute(String CQLStament){
        try {
            if (!driver.isUpService())
                throw new CassandraInterpreterException(MessageConstants.ERROR_SERVICE_NOT_UPPER);
            driver.executeCommand(CQLStament);
            return MessageConstants.INSERT_CORRECT;
        }catch(InvalidQueryException e){
            throw new CassandraInterpreterException(MessageConstants.ERROR_CQL_SYNTAX);
        }
    }
}
