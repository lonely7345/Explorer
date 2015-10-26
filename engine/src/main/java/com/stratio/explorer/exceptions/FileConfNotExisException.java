package com.stratio.explorer.exceptions;


public class FileConfNotExisException extends RuntimeException{

    public FileConfNotExisException(String errorMessage){
        super(errorMessage);
    }

    public String getMessage(){

        return super.getMessage();
    }
}
