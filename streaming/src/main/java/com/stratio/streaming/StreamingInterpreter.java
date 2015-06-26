package com.stratio.streaming;

import java.util.List;
import java.util.Properties;

import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.stratio.streaming.api.StratioStreamingAPI;
import com.stratio.streaming.commons.exceptions.StratioStreamingException;
import com.stratio.streaming.utils.StreamingApiWrapper;
import com.stratio.streaming.utils.StreamingSyntaxParser;

/**
 * Created by idiaz on 23/06/15.
 */

public class StreamingInterpreter extends Interpreter {
    StratioStreamingAPI api;
    StreamingApiWrapper wrapper;
    StreamingSyntaxParser parser;

    static {
        Interpreter.register("str", StreamingInterpreter.class.getName());
    }

    public StreamingInterpreter(Properties property) {
        super(property);
        api = new StratioStreamingAPI();
        wrapper = new StreamingApiWrapper(api);
        parser= new StreamingSyntaxParser(wrapper);

    }

    @Override public void open() {
        String[] kafka = System.getenv("KAFKA").split(":");
        String kafkaServer = kafka[0];
        int kafkaPort = Integer.parseInt(kafka[1]);
        String[] zookeeper = System.getenv("ZOOKEEPER").split(":");
        String zkServer = zookeeper[0];
        int zkPort = Integer.parseInt(zookeeper[1]);

        if (kafkaServer != null && kafkaPort >= 0 && zkServer != null && zkPort >= 0) { // if there is no
            // configuration set, it won't initialize
            api.initializeWithServerConfig(kafkaServer, kafkaPort, zkServer, zkPort);
            System.out.println("Streaming connection established");
        }
        if (!api.isInit()) {
            System.out.println("Streaming not initialized");
        }
    }

    @Override public void close() {
        api.close();
    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {
        try {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,parser.parse(st));
        } catch (StratioStreamingException e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR,"%text ".concat(e.getMessage()));
        }
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
