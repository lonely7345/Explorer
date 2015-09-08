package com.stratio.notebook.cassandra.exceptions;


public class ConnectionException extends RuntimeException{

    public ConnectionException(String errorMessage){
        super(errorMessage);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public String getMessage(){
=======
    public String getErrorMessage(){
>>>>>>> a
=======
    public String getMessage(){
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .

        return super.getMessage();
    }
}
