package com.stratio.notebook.cassandra.exceptions;


public class CassandraInterpreterException extends RuntimeException{


    public CassandraInterpreterException(Exception e,String errorMessgae){
        super(errorMessgae,e);
    }

    public String getMessage(){
        return super.getMessage();
    }
}
