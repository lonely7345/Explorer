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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.interpreter.InterpreterResult.Code;


public class SparkInterpreterIT {
	private SparkInterpreter repl;
	HashMap<String, Object> share = new HashMap<String, Object>();
	
	@Before
	public void setUp() throws Exception {
		Properties p = new Properties();
		p.put("share", new HashMap<String, Object>());
	    repl = SparkInterpreter.singleton();
	    if (repl == null) {
	    	repl = new SparkInterpreter(p);
			SparkInterpreter.setSingleton(repl);
			repl.open();
	    }
	}

	@After
	public void tearDown() throws Exception {
		repl.getSparkContext().stop();
	}

	@Test
	public void testBasicIntp() {
		assertEquals(InterpreterResult.Code.SUCCESS, repl.interpret("val a = 1\nval b = 2").code());
		
		// when interpret incomplete expression
		InterpreterResult incomplete = repl.interpret("val a = \"\"\"");
		assertEquals(InterpreterResult.Code.INCOMPLETE, incomplete.code());
		assertTrue(incomplete.message().length()>0); // expecting some error message
		/*
		assertEquals(1, repl.getValue("a"));
		assertEquals(2, repl.getValue("b"));
		repl.interpret("val ver = sc.version");
		assertNotNull(repl.getValue("ver"));		
		assertEquals("HELLO\n", repl.interpret("println(\"HELLO\")").message());
		*/
	}
	
	@Test
	public void testEndWithComment() {
		assertEquals(InterpreterResult.Code.SUCCESS, repl.interpret("val c=1\n//comment").code());
	}

	@Test
	public void testSparkSql(){
		repl.interpret("case class Person(name:String, age:Int)");
		repl.interpret(
				"val people = sc.parallelize(Seq(Person(\"moon\", 33), Person(\"jobs\", 51), Person(\"gates\", 51), Person(\"park\", 34)))");
		assertEquals(Code.SUCCESS, repl.interpret("people.take(3)").code());



		repl.interpret("case class Man(name:String, age:Int)");
		repl.interpret("val man = sc.parallelize(Seq(Man(\"moon\", 33), Man(\"jobs\", 51), Man(\"gates\", 51), Man(\"park\", 34)))");
		assertEquals(Code.SUCCESS, repl.interpret("man.take(3)").code());

	}

	@Test
	public void testReferencingUndefinedVal(){
		InterpreterResult result = repl.interpret("def category(min: Int) = {" +
				       "    if (0 <= value) \"error\"" +
                       "}");
		assertEquals(Code.ERROR, result.code());

	}
}
