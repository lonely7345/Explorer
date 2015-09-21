package com.stratio.notebook.interpreter;


public interface InterpreterDriver<T> {

    void connect();

    T executeCommand(String st);

}

