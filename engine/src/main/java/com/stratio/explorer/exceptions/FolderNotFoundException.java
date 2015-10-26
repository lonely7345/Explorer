package com.stratio.explorer.exceptions;


public class FolderNotFoundException extends RuntimeException{

    public FolderNotFoundException(String errorMessage){
        super(errorMessage);
    }

    public String getMessage(){

        return super.getMessage();
    }
}
