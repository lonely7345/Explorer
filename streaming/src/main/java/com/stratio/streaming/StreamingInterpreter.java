/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.streaming;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.streaming.api.StratioStreamingAPI;
import com.stratio.streaming.commons.exceptions.StratioStreamingException;
import com.stratio.streaming.utils.StreamingApiWrapper;
import com.stratio.streaming.utils.StreamingSyntaxParser;

import java.util.List;
import java.util.Properties;

/**
 * Created by idiaz on 23/06/15.
 */

public class StreamingInterpreter extends Interpreter {
    static {
        Interpreter.register("str", StreamingInterpreter.class.getName());
    }

    StratioStreamingAPI api;
    StreamingApiWrapper wrapper;
    StreamingSyntaxParser parser;

    public StreamingInterpreter(Properties property) {
        super(property);
        api = new StratioStreamingAPI();
        wrapper = new StreamingApiWrapper(api);
        parser = new StreamingSyntaxParser(wrapper);

    }

    @Override
    public void open() {
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

    @Override
    public void close() {
        api.close();
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public InterpreterResult interpret(String st) {
        try {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, parser.parse(st));
        } catch (StratioStreamingException e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, "%text ".concat(e.getMessage()));
        }
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
