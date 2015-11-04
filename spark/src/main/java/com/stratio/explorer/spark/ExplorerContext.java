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
package com.stratio.explorer.spark;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;

import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.notebook.NoteInterpreterLoader;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.notebook.form.Setting;
import com.stratio.explorer.notebook.form.Input;

public class ExplorerContext {

	private NoteInterpreterLoader noteInterpreterLoader;
	private PrintStream out;

	public ExplorerContext(SparkContext sc, SQLContext sql, NoteInterpreterLoader noteInterpreterLoader, PrintStream printStream) {
		this.sc = sc;
		this.sqlContext = sql;
		this.noteInterpreterLoader = noteInterpreterLoader;
		this.out = printStream;
	}

	public SparkContext sc;
	public SQLContext sqlContext;
	private Setting form;
	
	public DataFrame sql(String sql) {
		return sqlContext.sql(sql);
	}



	public Object input(String name) {
		return input(name, "");
	}
	
	public Object input(String name, Object defaultValue) {
		return form.input(name, defaultValue);
	}
	
	public Object select(String name, scala.collection.Iterable<Tuple2<Object, String>> options) {
		return select(name, "", options);
	}
	
	public Object select(String name, Object defaultValue, scala.collection.Iterable<Tuple2<Object, String>> options) {
		int n = options.size();
		Input.ParamOption[] paramOptions = new Input.ParamOption[n];
		Iterator<Tuple2<Object, String>> it = scala.collection.JavaConversions.asJavaIterable(options).iterator();

		int i=0;
		while (it.hasNext()) {
			Tuple2<Object, String> valueAndDisplayValue = it.next();
			paramOptions[i++] = new Input.ParamOption(valueAndDisplayValue._1(), valueAndDisplayValue._2());
		}
		
		return form.select(name, "", paramOptions);
	}
	
	public void setFormSetting(Setting o) {
		this.form = o;	
	}
	
	public void run(String lines) {
		String intpName = Paragraph.getRequiredReplName(lines);
		String scriptBody = Paragraph.getScriptBody(lines);
		Interpreter intp = noteInterpreterLoader.getRepl(intpName);
		InterpreterResult ret = intp.interpret(scriptBody);
		if (ret.code() == InterpreterResult.Code.SUCCESS) {
			out.println("%"+ret.type().toString().toLowerCase()+" "+ret.message());
		} else if (ret.code() == InterpreterResult.Code.ERROR) {
			out.println("Error: "+ret.message());
		} else if (ret.code() == InterpreterResult.Code.INCOMPLETE) {
			out.println("Incomplete");
		} else {
			out.println("Unknown error");
		}
	}
}
