package com.stratio.notebook.interpreter;


public interface InterpreterDriver {
    boolean isUpService();

    void executeCommand(String st);
}
