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
package com.stratio.explorer.notebook;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterFactory;
import com.stratio.explorer.interpreter.LazyOpenInterpreter;

/**
 * Repl loader per note
 */
public class NoteInterpreterLoader {
	private InterpreterFactory factory;
	Map<String, Interpreter> loadedInterpreters;
	static Map<String, Interpreter> loadedInterpretersStatic = Collections.synchronizedMap(new HashMap<String, Interpreter>());
	private boolean staticMode;
	
	public NoteInterpreterLoader(InterpreterFactory factory, boolean staticMode){
		this.factory = factory;
		this.staticMode = staticMode;
		if (staticMode) {
			loadedInterpreters = loadedInterpretersStatic;
		} else {
			loadedInterpreters = Collections.synchronizedMap(new HashMap<String, Interpreter>());
		}
	}
	
	public boolean isStaticMode (){
		return staticMode;
	}
	
	public synchronized Interpreter getRepl(String replName){

		String name = (replName!=null) ? replName : factory.getDefaultInterpreterName();
		if(loadedInterpreters.containsKey(name)) {
			return loadedInterpreters.get(name);
		} else {
			Properties p = new Properties();
			p.put("noteIntpLoader", this);
			Interpreter repl = factory.createRepl(name, p);
			LazyOpenInterpreter lazyIntp = new LazyOpenInterpreter(repl);
			loadedInterpreters.put(name, lazyIntp);
			return lazyIntp;
		}
	}
	
	public void destroyAll(){
		if(staticMode){
			// not destroying when it is static mode
			return;
		}
		
		Set<String> keys = loadedInterpreters.keySet();
		for(String k : keys) {
			Interpreter repl = loadedInterpreters.get(k);
			repl.close();
			repl.destroy();
			loadedInterpreters.remove(k);
		}
	}
	
}
