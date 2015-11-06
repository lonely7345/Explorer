package com.stratio.explorer.cassandra.exceptions;

/**
 * Launch when describe option is not velid .
 */
public class NotDescribeOptionException extends RuntimeException{

    public NotDescribeOptionException(String error){
        super(error);
    }
}
