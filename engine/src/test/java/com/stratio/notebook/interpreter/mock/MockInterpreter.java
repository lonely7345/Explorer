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
package com.stratio.notebook.interpreter.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.scheduler.Scheduler;
import com.stratio.notebook.scheduler.SchedulerFactory;

public class MockInterpreter extends Interpreter {
	
	public MockInterpreter(Properties property) {
		super(property);
	}

	static Map<String, Object> vars = new HashMap<String, Object>();

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public Object getValue(String name) {
		return vars.get(name);
	}

	@Override
	public InterpreterResult interpret(String st) {
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, st);
	}

	@Override
	public void bindValue(String name, Object o) {
		vars.put(name, o);
	}

	@Override
	public void cancel() {
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
		return SchedulerFactory.singleton().createOrGetFIFOScheduler("test_"+this.hashCode());
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		return null;
	}

}
