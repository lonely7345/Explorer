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
package com.stratio.notebook.notebook;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.notebook.form.Input;
import com.stratio.notebook.notebook.form.Setting;
import com.stratio.notebook.scheduler.Job;
import com.stratio.notebook.scheduler.JobListener;

/**
 * Paragraph is a representation of an execution unit
 */
public class Paragraph extends Job implements Serializable {
    private transient static final long serialVersionUID = -6328572073497992016L;
    private transient NoteInterpreterLoader replLoader;

    String title;
    String text;
    private Map<String, Object> config; // paragraph configs like isOpen, colWidth, etc...
    public final Setting settings; // form and parameter settings
    String replName;

    public Paragraph(JobListener listener, NoteInterpreterLoader replLoader) {
        super(generateId(), listener);
        this.replLoader = replLoader;
        title = null;
        text = null;
        settings = new Setting();
        config = new HashMap<String, Object>();
        replName = null;
    }

    private static String generateId() {
        return "paragraph_" + System.currentTimeMillis() + "_"
                + new Random(System.currentTimeMillis()).nextInt();
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequiredReplName() {
        if (config.get("interpreter") != null) {
            String replNameRaw = ((String) config.get("interpreter"));
            switch (replNameRaw) {
            case "ingestion":
                replName = "ing";
                break;
            case "shell":
                replName = "sh";
                break;
            case "crossdata":
                replName = "xdql";
                break;
            case "markdown":
                replName = "md";
                break;
            case "sql":
                replName = "sql";
                break;
            case "spark":
                replName = "s";
                break;
            case "streaming":
                replName = "str";
                break;
            case "cassandra":
                replName = "cql";
                break;
            }

        }
       if (replName != null) {
            return replName;
        }
        return getRequiredReplName(text);
    }

    public static String getRequiredReplName(String text) {

        if (text == null) {
            return null;
        }

        // get script head
        int scriptHeadIndex = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == ' ' || ch == '\n') {
                scriptHeadIndex = i;
                break;
            }
        }
        if (scriptHeadIndex == 0) {
            return null;
        }
        String head = text.substring(0, scriptHeadIndex);
        if (head.startsWith("%")) {
            return head.substring(1);
        } else {
            return null;
        }
    }

    private String getScriptBody() {
        return getScriptBody(text);
    }

    public static String getScriptBody(String text) {
        if (text == null) {
            return null;
        }

        String magic = getRequiredReplName(text);
        if (magic == null) {
            return text;
        }
        if (magic.length() + 2 >= text.length()) {
            return "";
        }
        return text.substring(magic.length() + 2);
    }

    public NoteInterpreterLoader getNoteReplLoader() {
        return replLoader;
    }

    public Interpreter getRepl(String name) {
        return replLoader.getRepl(name);
    }

    public List<String> completion(String buffer, int cursor) {
        String replName = getRequiredReplName(buffer);
        if (replName != null) {
            cursor -= replName.length() + 1;
        }
        String body = getScriptBody(buffer);
        Interpreter repl = getRepl(replName);
        if (repl == null) {
            return null;
        }

        return repl.completion(body, cursor);
    }

    public void setNoteReplLoader(NoteInterpreterLoader repls) {
        this.replLoader = repls;
    }

    public InterpreterResult getResult() {
        if (String.class.isInstance(getReturn())) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, String.class.cast(getReturn()));
        }
        return (InterpreterResult) getReturn();

    }

    @Override
    public int progress() {
        String replName = getRequiredReplName();
        Interpreter repl = getRepl(replName);
        if (repl != null) {
            return repl.getProgress();
        } else {
            return 0;
        }
    }

    @Override
    public Map<String, Object> info() {

        return null;
    }

    @Override
    protected Object jobRun(){
        String replName = getRequiredReplName();
        Interpreter repl = getRepl(replName);
        logger().info("run paragraph {} using {} " + repl, getId(), replName);
        if (repl == null) {
            logger().error("Can not find interpreter name " + repl);
            throw new RuntimeException("Can not find interpreter for " + getRequiredReplName());
        }

        String script = getScriptBody();
        // inject form
        if (repl.getFormType() == Interpreter.FormType.NATIVE) {
            settings.clear();
            repl.bindValue("form", settings); // user code will dynamically create inputs
        } else if (repl.getFormType() == Interpreter.FormType.SIMPLE) {
            String scriptBody = getScriptBody();
            Map<String, Input> inputs = Input.extractSimpleQueryParam(scriptBody); // inputs will be built
            // from script body
            settings.setForms(inputs);
            script = Input.getSimpleQuery(settings.getParams(), scriptBody);
        }
        // inject itself for streaming
        repl.bindValue("paragraph", this);
        logger().info("RUN : " + script);
        return repl.interpret(script);
    }

    @Override
    protected boolean jobAbort() {
        Interpreter repl = getRepl(getRequiredReplName());
        repl.cancel();
        return true;
    }

    private Logger logger() {
        Logger logger = LoggerFactory.getLogger(Paragraph.class);
        return logger;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

}
