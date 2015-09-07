package com.stratio.notebook.cassandra.exceptions;


public class ConnectionException extends RuntimeException{

    public ConnectionException(String errorMessage){
        super(errorMessage);
    }

<<<<<<< HEAD
    public String getMessage(){
=======
    public String getErrorMessage(){
>>>>>>> a

        return super.getMessage();
    }
}
