package com.stratio.notebook.mongodb.command;

/**
 * This Interfaz execute mongo commands.
 * Created by jmgomez on 11/09/15.
 */
public interface IMongoCommandExecutor {
    /**
     * Excecute a mongoCommand.
     * @param st command.
     * @return result Command.
     */
    String execute(String st);
}
