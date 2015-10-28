/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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
package com.stratio.explorer.interpreter;

import java.util.List;
import java.util.Properties;

import com.stratio.explorer.scheduler.Scheduler;

public class ClassloaderInterpreter extends Interpreter {

	private ClassLoader cl;
	private Interpreter intp;

	public ClassloaderInterpreter(Interpreter intp, ClassLoader cl, Properties property) {
		super(property);
		this.cl = cl;
		this.intp = intp;
	}
	
	public Interpreter getInnerRepl(){
		return intp;
	}
	
	public ClassLoader getClassloader(){
		return cl;
	}

	@Override
	public Object getValue(String name) {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			return intp.getValue(name);
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}
	}

	@Override
	public InterpreterResult interpret(String st) {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			return intp.interpret(st);
		} catch (Exception e){
			e.printStackTrace();
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}
	}

	@Override
	public void bindValue(String name, Object o) {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			intp.bindValue(name, o);
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}
	}

	@Override
	public void open() {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			intp.open();
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}		
	}

	@Override
	public void close() {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			intp.close();
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}	
	}

	@Override
	public void cancel() {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			intp.cancel();
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}	
	}

	@Override
	public FormType getFormType() {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			return intp.getFormType();
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}	
	}

	@Override
	public int getProgress() {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			return intp.getProgress();
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}	
	}
	
	@Override
	public Scheduler getScheduler() {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			return intp.getScheduler();
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}	
	}
	
	@Override
	public List<String> completion(String buf, int cursor) {
		ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		try {
			return intp.completion(buf, cursor);
		} catch (Exception e){
			throw new InterpreterException(e);
		} finally {
			cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(oldcl);
		}	
	}
}
