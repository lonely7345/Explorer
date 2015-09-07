package com.stratio.notebook.interpreter;


<<<<<<< HEAD

public interface InterpreterDriver<T> {

    void connect();

    T executeCommand(String st);

=======
public interface InterpreterDriver {

    void connect();

    Object executeCommand(String st);
>>>>>>> a
}
