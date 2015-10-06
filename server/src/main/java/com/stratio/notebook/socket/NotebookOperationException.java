package com.stratio.notebook.socket;

/**
 * A exception in while a notebook operation.
 * Created by jmgomez on 3/09/15.
 */
public class NotebookOperationException extends Exception{

    /**
     * Constructor.
     * @param message the message.
     * @param exception a original excepcion.
     */
    public NotebookOperationException(String message, Exception exception){
        super(message,exception);
    }

}
