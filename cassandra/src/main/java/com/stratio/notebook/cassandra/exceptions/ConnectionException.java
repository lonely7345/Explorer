package com.stratio.notebook.cassandra.exceptions;

/**
 * This Exception is throw when a error happens during connection.
 */
public class ConnectionException extends RuntimeException{

    /**
     * Constructor.
     * @param e original exception.
     * @param errorMessage message.
     */
    public ConnectionException(Exception e,String errorMessage){
        super(errorMessage,e);
    }

}
