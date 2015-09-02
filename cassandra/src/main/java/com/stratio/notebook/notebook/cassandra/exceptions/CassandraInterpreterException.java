package com.stratio.notebook.notebook.cassandra.exceptions;


public class CassandraInterpreterException extends RuntimeException{

    private String errorMessage;

    public CassandraInterpreterException(String errorMessage){
        this.errorMessage =  errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
