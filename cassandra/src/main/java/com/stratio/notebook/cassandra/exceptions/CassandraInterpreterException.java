package com.stratio.notebook.cassandra.exceptions;


public class CassandraInterpreterException extends RuntimeException{

    public CassandraInterpreterException(String errorMessage){
        super(errorMessage);
    }

    public String getErrorMessage(){
        return super.getMessage();
    }
}
