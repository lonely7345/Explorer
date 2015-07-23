package com.stratio.ingestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.stratio.ingestion.utils.IngestionAgent;
import com.stratio.ingestion.utils.IngestionParserException;
import com.stratio.ingestion.utils.IngestionParserResult;
import com.stratio.ingestion.utils.IngestionSyntaxParser;
import com.stratio.ingestion.utils.IngestionUtils;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionInterpreter extends Interpreter {
    Logger logger = LoggerFactory.getLogger(IngestionInterpreter.class);
    int CMD_TIMEOUT = 600000;
    String ingestionHome;
    public static final String INGESTION_SETTING_HOME = "ingestion.home";

    static {
        Interpreter.register("ing", IngestionInterpreter.class.getName());
    }

    public IngestionInterpreter(Properties property) {
        super(property);
        ingestionHome = "";
    }

    @Override public void open() {
        Map<String, String> settings = IngestionUtils.readConfig();
        ingestionHome = settings.get(INGESTION_SETTING_HOME);
    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {

        if (ingestionHome.isEmpty()) {
            open();
            return new InterpreterResult(InterpreterResult.Code.ERROR, "%text Ingestion is not installed correctly. "
                    + "INGESTION_HOME Is not set");
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
                //                System.out.println(agentStart.toString());
                shellCommand.append("exec \"")
                        .append(ingestionHome)
                        .append("/bin/flume-ng\" agent --conf ")
                        .append(ingestionHome).append("/conf --conf-file ")
                        .append(agentStart.getFilepath())
                        .append(" --name ")
                        .append(agentStart.getName())
                        .append(" -Dflume.monitoring.type=http -Dflume.monitoring.port=")
                        .append(String.valueOf(agentStart.getPort()))
                        .append("> /dev/null & ");

                bashResult = IngestionUtils.executeBash(shellCommand.toString(), 2000);
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
//                System.out.println("Agent stop -> " + shellCommand.toString());
                IngestionUtils.executeBash(shellCommand.toString(), CMD_TIMEOUT);
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, interpreterResult.append("Agent "
                        + "apparently stopped ").toString());

            } catch (IOException e) {
                if (e.getMessage().contains("143")) {
                    return new InterpreterResult(InterpreterResult.Code.SUCCESS, interpreterResult.append("Agent "
                            + "apparently stopped ").toString());
                } else {
                    return new InterpreterResult(InterpreterResult.Code.ERROR, "%text " + e.getMessage());
                }
            }
        case CHANNELS_STATUS:
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

    @Override public void cancel() {
    }

    @Override public void bindValue(String name, Object o) {

    }

    @Override public FormType getFormType() {
        return null;
    }

    @Override public int getProgress() {
        return 0;
    }

    @Override public List<String> completion(String buf, int cursor) {
        return null;
    }
}
