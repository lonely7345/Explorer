package com.stratio.notebook.mongodb.command;

import com.mongodb.MongoClient;

/**
 * This Class must decide the command to execut.
 * Created by jmgomez on 11/09/15.
 */
public class CommandDecider {

    /**
     * Private Constructor.
      */
private CommandDecider(){}

    public static IMongoCommandExecutor decideCommand(String command,MongoClient mongoClient){
        IMongoCommandExecutor iMongoCommandExecutor = null;
        if (command!=null){
            String trimCommand = command.trim();
            if ("show dbs".equalsIgnoreCase(trimCommand)){
                iMongoCommandExecutor =new MongoCommandExecutorShowDBS(mongoClient);
            }
        }
        return iMongoCommandExecutor;
    }
}
