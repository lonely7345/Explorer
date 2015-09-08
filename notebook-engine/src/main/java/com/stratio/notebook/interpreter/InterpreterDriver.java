package com.stratio.notebook.interpreter;


<<<<<<< HEAD
<<<<<<< HEAD

public interface InterpreterDriver<T> {

    void connect();

    T executeCommand(String st);

=======
public interface InterpreterDriver {

    void connect();

    Object executeCommand(String st);
>>>>>>> a
=======
public interface InterpreterDriver<T> {

    void connect();

    T executeCommand(String st);
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .
}
