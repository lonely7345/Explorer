package com.stratio.notebook.interpreter;


public interface InterpreterDriver {

    void connect();

    Object executeCommand(String st);
}
