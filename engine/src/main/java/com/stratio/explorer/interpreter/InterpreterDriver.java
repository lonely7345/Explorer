package com.stratio.explorer.interpreter;


import com.stratio.explorer.gateways.Connector;

public interface InterpreterDriver<T> {

    /**
     *  Execute command in one interpreter
     * @param st string to exec
     * @return Table
     */
    T executeCommand(String st);

    /**
     *
     * @return connector to persistence
     */
    Connector getConnector();

}

