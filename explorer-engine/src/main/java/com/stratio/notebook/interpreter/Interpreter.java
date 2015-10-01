/**
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
		registeredInterpreters.put(name, className);
	}
	
	public abstract void open();
	public abstract void close();
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
