package com.stratio.notebook.cassandra.exceptions;


public class ConnectionException extends RuntimeException{

    public ConnectionException(Exception e,String errorMessage){
        super(errorMessage,e);
    }

    public String getMessage(){

        return super.getMessage();
    }
}
