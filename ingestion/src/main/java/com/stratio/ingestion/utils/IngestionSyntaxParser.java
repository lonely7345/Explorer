package com.stratio.ingestion.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionSyntaxParser {

    /**
     * The Log.
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public IngestionSyntaxParser() {
    }

    public enum Command {
        AGENT_START,
        AGENT_STOP,
        CHANNELS_STATUS,
        LIST_PROPERTIES,
        HELP
    }

    public IngestionParserResult parse(String input) throws IngestionParserException {
        Matcher matcher;
        input = input.trim(); // normalize
        Map<Command, Pattern> commandPatterns = new HashMap<>();
        commandPatterns.put(Command.AGENT_START, Pattern.compile("(agent start)(.*)")); //agent start
        commandPatterns.put(Command.AGENT_STOP, Pattern.compile("(agent stop)(.*)")); //agent stop
        commandPatterns.put(Command.CHANNELS_STATUS, Pattern.compile("(channel status)(.*)")); //channel status
        commandPatterns.put(Command.LIST_PROPERTIES, Pattern.compile("(list properties)(.*)")); //list properties
        commandPatterns.put(Command.HELP, Pattern.compile("(help)(.*)")); //help
        for (Map.Entry<Command, Pattern> p : commandPatterns.entrySet()) {
            matcher = p.getValue().matcher(input);
            if (matcher.find()) {
                String filtered = matcher.group(2);
                switch (p.getKey()) {
                case AGENT_START:
                    logger.info("Syntax parser agent start");
                    return agentParse(Command.AGENT_START, filtered);
                case AGENT_STOP:
                    logger.info("Syntax parser agent stop");
                    return agentParse(Command.AGENT_STOP, filtered);
                case CHANNELS_STATUS:
                    return channelStatus(filtered);
                case LIST_PROPERTIES:
                    return new IngestionParserResult(Command.LIST_PROPERTIES);
                case HELP:
                    return new IngestionParserResult(Command.HELP);
                }
            }
        }
        throw new IngestionParserException("Invalid Ingestion command");
    }

    private IngestionParserResult agentParse(Command c, String input) throws IngestionParserException {
        Pattern agentPattern = Pattern.compile("( --file) (([/\\w]+[-.]*[\\w]*)+)( --port) ([\\d]+)");
        Matcher agentMatch = agentPattern.matcher(input);
        IngestionAgent agent = new IngestionAgent();
        if (agentMatch.find()) {
            agent.setFilepath(agentMatch.group(2));
            agent.setPort(Integer.parseInt(agentMatch.group(5)));
            try {
                agent.setName(IngestionUtils.getAgentName(agent.getFilepath()));
            } catch (IOException e) {
                throw new IngestionParserException(e);
            }
            if (c == Command.AGENT_START) {
                return new IngestionParserResult(Command.AGENT_START, agent);
            } else {
                return new IngestionParserResult(Command.AGENT_STOP, agent);
            }
        }

        if (c == Command.AGENT_START) {
            throw new IngestionParserException("Invalid agent start syntax. Use the following pattern as guide: agent "
                    + "start --file /path/to/file --port 0000");
        } else if(c ==Command.AGENT_STOP){
            throw new IngestionParserException("Invalid agent stop syntax. Use the following pattern as guide: agent "
                    + "stop --file /path/to/file --port 0000");
        } else throw new IngestionParserException("Invalid command");
    }

    private IngestionParserResult channelStatus (String input) throws IngestionParserException {
        Pattern channelPattern = Pattern.compile("( --port) ([\\d]+)");
        Matcher channelMatch = channelPattern.matcher(input);
        IngestionAgent agent = new IngestionAgent();
        if (channelMatch.find()) {
            agent.setPort((Integer.parseInt(channelMatch.group(2))));
            return new IngestionParserResult(Command.CHANNELS_STATUS,agent);
        }

        throw new IngestionParserException("Invalid channel status syntax. Use the following pattern as guide: "
                + "channel status --port 0000 ");
    }

}
