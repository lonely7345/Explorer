package com.stratio.ingestion;

import com.stratio.ingestion.utils.*;
import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionInterpreter extends Interpreter {
    /**
     * The Log.
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public static final String INGESTION_SETTING_HOME = "ingestion.home";

    static {
        Interpreter.register("ing", IngestionInterpreter.class.getName());
    }

    int CMD_TIMEOUT = 3000;//3secs
    String ingestionHome;

    public IngestionInterpreter(Properties property) {
        super(property);
        ingestionHome = "";
    }

    @Override
    public void open() {
        Map<String, String> settings = IngestionUtils.readConfig();
        ingestionHome = settings.get(INGESTION_SETTING_HOME);
    }

    @Override
    public void close() {

    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public InterpreterResult interpret(String st) {

        if (ingestionHome.isEmpty()) {
            open();
            if (ingestionHome.isEmpty()) {
                return new InterpreterResult(InterpreterResult.Code.ERROR,
                        "%text Ingestion is not installed correctly. "
                                + "INGESTION_HOME Is not set");
            }
        }


        IngestionSyntaxParser parser = new IngestionSyntaxParser();
        IngestionParserResult parserResult = null;
        try {
            parserResult = parser.parse(st);
        } catch (IngestionParserException e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, "%text " + e.getMessage());
        }

        StringBuilder shellCommand = new StringBuilder();
        StringBuilder interpreterResult = new StringBuilder();
        String bashResult;
        interpreterResult.append("%text ");

        switch (parserResult.getCommand()) {
            case AGENT_START:

                try {
                    IngestionAgent agentStart = parserResult.getAgent();
                    String agentName = agentStart.getName();
                    String agentFilepath = agentStart.getFilepath();
                    int agentPort = agentStart.getPort();

                    logger.info("Ingestion interpreter @ agent start $INGESTION_HOME -> " + ingestionHome);
                    logger.info("Ingestion interpreter @ agent start agent filepath -> " + agentFilepath);
                    logger.info("Ingestion interpreter @ agent start agent name -> " + agentName);
                    logger.info("Ingestion interpreter @ agent start agent port -> " + agentPort);

                    shellCommand.append("exec \"")
                            .append(ingestionHome)
                            .append("/bin/flume-ng\" agent --conf ")
                            .append(ingestionHome).append("/conf --conf-file ")
                            .append(agentFilepath)
                            .append(" --name ")
                            .append(agentName)
                            .append(" -Dflume.monitoring.type=http -Dflume.monitoring.port=")
                            .append(String.valueOf(agentPort))
                            .append("> /dev/null & ");

                    logger.info("Ingestion interpreter @ agent start command -> " + shellCommand.toString());
                    DefaultExecuteResultHandler handler = IngestionUtils.executeBash(shellCommand.toString());
                    long initTime = System.currentTimeMillis();
                    while (!handler.hasResult()) {
                        if (System.currentTimeMillis() > (initTime + CMD_TIMEOUT)) {
                            handler.onProcessComplete(999);
                        }
                    }
                    if (handler.getException() != null) {
                        return new InterpreterResult(InterpreterResult.Code.ERROR, "%text " + handler.getExitValue() + " " +
                                handler
                                        .getException()
                                        .getMessage());
                    }
                    bashResult = "Agent started";
                    return new InterpreterResult(InterpreterResult.Code.SUCCESS, bashResult);
                } catch (IOException e) {
                    return new InterpreterResult(InterpreterResult.Code.ERROR, "%text " + e.getMessage());
                }
            case AGENT_STOP:
                try {
                    IngestionAgent agentStop = parserResult.getAgent();
                    shellCommand.append("ps auxww | grep flume | grep ")
                            .append(agentStop.getPort())
                            .append("| awk '{ print $2 }' | xargs kill -15 ");
                    IngestionUtils.executeBash(shellCommand.toString());
                    return new InterpreterResult(InterpreterResult.Code.SUCCESS, interpreterResult.append("Agent "
                            + "apparently stopped ").toString());

                } catch (IOException e) {
                    if (e.getMessage().contains("143")) { //after kill a process always 143 exit code
                        return new InterpreterResult(InterpreterResult.Code.SUCCESS, interpreterResult.append("Agent "
                                + "apparently stopped ").toString());
                    } else {
                        return new InterpreterResult(InterpreterResult.Code.ERROR, "%text " + e.getMessage());
                    }
                }
            case CHANNELS_STATUS:
                try {
                    String json = IngestionUtils.getAgentStatus(parserResult.getAgent().getPort());
                    if (json.length() > 3) {
                        Map<String, String> channelsStatus = IngestionUtils.getChannelsStatus(json);
                        String channelStatusResult = "";
                        for (String channel : channelsStatus.keySet()) {
                            channelStatusResult +=
                                    "Channel ".concat(channel).concat(": ")
                                            .concat(channelsStatus.get(channel)).concat("\n");
                        }
                        return new InterpreterResult(InterpreterResult.Code.SUCCESS, "%text " + channelStatusResult);
                    }
                } catch (IOException e) {
                    return new InterpreterResult(InterpreterResult.Code.ERROR, "%text " + e.getMessage());
                }
                break;
            case LIST_PROPERTIES:
                List<String> files = new ArrayList<>();
                StringBuilder listResult = new StringBuilder();
                IngestionUtils.getIngestionPropertiesFiles(files, ingestionHome);
                if (!files.isEmpty()) {
                    for (String file : files) {
                        listResult.append(file).append("\n");
                    }
                    return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                            "%text ".concat(listResult.toString()));
                }
                return new InterpreterResult(InterpreterResult.Code.ERROR, "%text No .properties files found");
            case HELP:
                return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                        interpreterResult.append(IngestionUtils.help())
                                .toString());
        }

        return new InterpreterResult(InterpreterResult.Code.ERROR,
                "%text Ingestion command not recognized, type help"
                        + " for more info");
    }

    @Override
    public void cancel() {
    }

    @Override
    public void bindValue(String name, Object o) {

    }

    @Override
    public FormType getFormType() {
        return null;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public List<String> completion(String buf, int cursor) {
        return null;
    }
}
