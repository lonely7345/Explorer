package com.stratio.explorer.cassandra.exceptions;

/**
 *  This exception occur when not have any property
 */
public class NotPropertyFoundException extends RuntimeException{

    /**
     * Constructor.
     * @param e original exception.
     * @param errorMessage message.
     */
    public NotPropertyFoundException(Exception e,String errorMessage){
        super(errorMessage,e);
    }
}
