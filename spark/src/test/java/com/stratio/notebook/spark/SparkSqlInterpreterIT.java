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

package com.stratio.notebook.spark;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.interpreter.InterpreterResult.Type;

public class SparkSqlInterpreterIT {

	private SparkInterpreter repl;
	private SparkSqlInterpreter sql;

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
		sql = new SparkSqlInterpreter(p);
		sql.open();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() {
		repl.interpret("case class Test(name:String, age:Int)");
		repl.interpret("val test = sc.parallelize(Seq(Test(\"moon\", 33), Test(\"jobs\", 51), Test(\"gates\", 51), Test(\"park\", 34))).toDF()");
		repl.interpret("test.registerTempTable(\"test\")");

		InterpreterResult ret = sql.interpret("select name, age from test where age < 40");
		assertEquals(InterpreterResult.Code.SUCCESS, ret.code());
		assertEquals(Type.TABLE, ret.type());
		assertEquals("name\tage\nmoon\t33\npark\t34\n", ret.message());

		assertEquals(InterpreterResult.Code.ERROR, sql.interpret("select wrong syntax").code());
		assertEquals(InterpreterResult.Code.ERROR, sql.interpret("select case when name==\"aa\" then name else name end from people").code());
	}

	@Test
	public void testStruct(){
		repl.interpret("case class Person(name:String, age:Int)");
		repl.interpret("case class People(group:String, person:Person)");
		repl.interpret("val gr = sc.parallelize(Seq(People(\"g1\", Person(\"moon\",33)), People(\"g2\", Person"
				+ "(\"sun\",11)))).toDF()");
		repl.interpret("gr.registerTempTable(\"gr\")");
		InterpreterResult ret = sql.interpret("select * from gr");
		assertEquals(InterpreterResult.Code.SUCCESS, ret.code());
	}

	@Test
	public void test_null_value_in_row() {
		InterpreterResult ret = null;
		repl.interpret("import org.apache.spark.sql._");
		repl.interpret("def toInt(s:String): Any = {try { s.trim().toInt} catch {case e:Exception => null}}");
		repl.interpret("import org.apache.spark.sql.types");
		repl.interpret("import org.apache.spark.sql.types._");
		repl.interpret(
				"val schema = StructType(Seq(StructField(\"name\", StringType, false),StructField(\"age\" , IntegerType, true),StructField(\"other\" , StringType, false)))");
	     ret = repl.interpret(
				"val csv = sc.parallelize(Seq((\"jobs, 51, apple\"), (\"gates, , microsoft\")))");
		ret = repl.interpret("val raw = csv.map(_.split(\",\")).map(p => Row(p(0),toInt(p(1)),p(2)))");
		ret = repl.interpret("val people = sqlContext.createDataFrame(raw,schema) ");
		ret = repl.interpret(
				"people.registerTempTable(\"people\")");

		ret = sql.interpret("select name, age from people where name = 'gates'");
		System.err.println("RET=" + ret.message());
		assertEquals(InterpreterResult.Code.SUCCESS, ret.code());
		assertEquals(Type.TABLE, ret.type());
		assertEquals("name\tage\ngates\tnull\n", ret.message());
	}
}
