package com.stratio.notebook.cassandra.exceptions;


/**
 * This exception is throw when a error in cassandra interpreter happens.
 */
public class CassandraInterpreterException extends RuntimeException{


    /**
     * Constructor.
     * @param e original exception.
     * @param errorMessgae the message.
     */
    public CassandraInterpreterException(Exception e,String errorMessgae){
        super(errorMessgae,e);
    }

}
