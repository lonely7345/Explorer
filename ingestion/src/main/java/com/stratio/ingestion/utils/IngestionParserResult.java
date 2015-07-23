package com.stratio.ingestion.utils;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionParserResult {
    private IngestionSyntaxParser.Command command;
    private IngestionAgent agent;

    public IngestionParserResult(IngestionSyntaxParser.Command command) {
        this.command = command;
        this.agent=null;
    }

    public IngestionParserResult(IngestionSyntaxParser.Command command, IngestionAgent agent) {
        this.command = command;
        this.agent = agent;
    }

    public IngestionSyntaxParser.Command getCommand() {
        return command;
    }

    public void setCommand(IngestionSyntaxParser.Command command) {
        this.command = command;
    }

    public IngestionAgent getAgent() {
        return agent;
    }

    public void setAgent(IngestionAgent agent) {
        this.agent = agent;
    }
}
