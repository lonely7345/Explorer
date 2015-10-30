/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.markdown;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.markdown4j.Markdown4jProcessor;

import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.interpreter.InterpreterResult.Code;
import com.stratio.explorer.scheduler.Scheduler;
import com.stratio.explorer.scheduler.SchedulerFactory;

public class Markdown extends Interpreter {
	private Markdown4jProcessor md;

	static {
		Interpreter.register("md", Markdown.class.getName());
	}
	
	public Markdown(Properties property){
		super(property);
	}	
	
	@Override
	public void open() {
		md = new Markdown4jProcessor();
	}

	@Override
	public void close() {
	}

	@Override
	public Object getValue(String name) {
		return null;
	}

	@Override
	public InterpreterResult interpret(String st) {
		String html;
		try {
			html = md.process(st);
		} catch (IOException e) {
			return new InterpreterResult(Code.ERROR, e.getMessage());
		}
		return new InterpreterResult(Code.SUCCESS, "%html "+html);
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
		return SchedulerFactory.singleton().createOrGetParallelScheduler(Markdown.class.getName()+this.hashCode(), 5);
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		return null;
	}
}
