package com.stratio.notebook.interpreter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.notebook.scheduler.Scheduler;
import com.stratio.notebook.scheduler.SchedulerFactory;

//TODO try to refactor to use SOLID principles "Interface segregation principle". This class has a lot of method which are not implement to the concrete clas (they are void)
public abstract class Interpreter {
	static Logger logger = LoggerFactory.getLogger(Interpreter.class);

	private Properties property;
	
	public Interpreter(Properties property){
		this.property = property;
	}
	
	public static enum FormType {
		NATIVE,
		SIMPLE,
		NONE
	}
	
	public static enum SchedulingMode {
		FIFO,
		PARALLEL
	}
	
	public static Map<String, String> registeredInterpreters = Collections.synchronizedMap(new HashMap<String, String>());
	
	public static void register(String name, String className) {
		logger.debug(String.format("Register [ %s ] with [%s]",name,className));
		registeredInterpreters.put(name, className);
	}


	/**
	 * This method must open the interpreter connection with the system which interact.
	 */
	public abstract void open();
	/**
	 * This method must close the interpreter connection with the system which interact.
	 */
	public abstract void close();
	//TODO comment this methods.
	public abstract Object getValue(String name);
	public abstract InterpreterResult interpret(String st);
	public abstract void cancel();
	public abstract void bindValue(String name, Object o);
	public abstract FormType getFormType();
	public abstract int getProgress();
	
	public Scheduler getScheduler() {
		return SchedulerFactory.singleton().createOrGetFIFOScheduler("interpreter_"+this.hashCode());

	}
	
	public void destroy() {
		getScheduler().stop();
	}
	
	public abstract List<String> completion(String buf, int cursor);

	public Properties getProperty() {
		return property;
	}

	public void setProperty(Properties property) {
		this.property = property;
	}
	
	
}
