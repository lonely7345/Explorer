package com.stratio.explorer.socket;

/**
 * A exception in while a notebook operation.
 * Created by jmgomez on 3/09/15.
 */
public class ExplorerOperationException extends Exception{

    /**
     * Constructor.
     * @param message the message.
     * @param exception a original excepcion.
     */
    public ExplorerOperationException(String message, Exception exception){
        super(message,exception);
    }

}
