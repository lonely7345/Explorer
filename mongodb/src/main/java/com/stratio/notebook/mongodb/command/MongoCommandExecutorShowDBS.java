package com.stratio.notebook.mongodb.command;

import com.mongodb.MongoClient;

/**
 * This class must execute a mongoCommand and process the result.
 * Created by jmgomez on 11/09/15.
 */
public class MongoCommandExecutorShowDBS implements IMongoCommandExecutor {
    MongoClient mongoClient;

    /**
     * Constructor.
     * @param mongoClient a connection to mongo.
     */
    public MongoCommandExecutorShowDBS(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    /**
     * This method execute a mongo command show dbs.
     * @param st the command.
     * @return the command return.
     */
    @Override
    public String execute(String st) {
        StringBuilder sb = new StringBuilder("");
        for (String databaseName: mongoClient.listDatabaseNames()){
            sb.append(databaseName).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
