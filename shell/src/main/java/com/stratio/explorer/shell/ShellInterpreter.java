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
package com.stratio.explorer.shell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.interpreter.InterpreterResult.Code;
import com.stratio.explorer.scheduler.Scheduler;
import com.stratio.explorer.scheduler.SchedulerFactory;

public class ShellInterpreter extends Interpreter {
	Logger logger = LoggerFactory.getLogger(ShellInterpreter.class);
	int CMD_TIMEOUT = 600000;
	
	static {
		Interpreter.register("sh", ShellInterpreter.class.getName());
	}
	
	public ShellInterpreter(Properties property) {
		super(property);
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public Object getValue(String name) {
		return null;
	}

	@Override
	public InterpreterResult interpret(String cmd) {
		logger.info("Run shell command '"+cmd+"'");
		long start = System.currentTimeMillis();
		CommandLine cmdLine = CommandLine.parse("bash");
		cmdLine.addArgument("-c", false);
		cmdLine.addArgument(cmd, false);
		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		executor.setStreamHandler(new PumpStreamHandler(outputStream));

		executor.setWatchdog(new ExecuteWatchdog(CMD_TIMEOUT));
		try {
			int exitValue = executor.execute(cmdLine);
			return new InterpreterResult(InterpreterResult.Code.SUCCESS, outputStream.toString());
		} catch (ExecuteException e) {
			logger.error("Can not run "+cmd, e);
			return new InterpreterResult(Code.ERROR, e.getMessage());
		} catch (IOException e) {
			logger.error("Can not run "+cmd, e);
			return new InterpreterResult(Code.ERROR, e.getMessage());
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
		return FormType.SIMPLE;
	}

	@Override
	public int getProgress() {
		return 0;
	}
	
	@Override
	public Scheduler getScheduler() {
		return SchedulerFactory.singleton().createOrGetFIFOScheduler(ShellInterpreter.class.getName()+this.hashCode());
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		return null;
	}

}
