/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.notebook.interpreter;

import java.util.List;

import com.stratio.notebook.scheduler.Scheduler;

public class LazyOpenInterpreter extends Interpreter{

	private Interpreter intp;
	boolean opened = false;

	public LazyOpenInterpreter(Interpreter intp) {
		super(intp.getProperty());
		this.intp = intp;
	}

	@Override
	public void open() {
		if(opened==true) {
			return;
		}
		
		synchronized(this){
			if(opened==false){
				intp.open();
				opened = true;
			}
		}
	}

	@Override
	public void close() {
		synchronized(this){
			if(opened==true){
				intp.close();
				opened = false;
			}
		}
	}

	@Override
	public Object getValue(String name) {
		open();
		return intp.getValue(name);
	}

	@Override
	public InterpreterResult interpret(String st) {
		open();
		return intp.interpret(st);
	}

	@Override
	public void cancel() {
		open();
		intp.cancel();
	}

	@Override
	public void bindValue(String name, Object o) {
		open();
		intp.bindValue(name, o);
	}

	@Override
	public FormType getFormType() {
		return intp.getFormType();
	}

	@Override
	public int getProgress() {
		open();
		return intp.getProgress();
	}

	@Override
	public Scheduler getScheduler() {
		return intp.getScheduler();
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		open();
		return intp.completion(buf, cursor);
	}
}
