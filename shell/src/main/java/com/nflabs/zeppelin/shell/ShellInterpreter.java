package com.nflabs.zeppelin.shell;

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

import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.interpreter.Interpreter.SchedulingMode;
import com.nflabs.zeppelin.interpreter.InterpreterResult.Code;
import com.nflabs.zeppelin.scheduler.Scheduler;
import com.nflabs.zeppelin.scheduler.SchedulerFactory;

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
