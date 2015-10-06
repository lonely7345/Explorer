package com.stratio.notebook.interpreter;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stratio.notebook.conf.ExplorerConfiguration;

public class InterpreterFactoryUT {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasic() {
		InterpreterFactory factory = new InterpreterFactory(ExplorerConfiguration.create());
		Interpreter repl1 = factory.createRepl("mock", "com.nflabs.zeppelin.interpreter.mock.MockInterpreter", new Properties());
		repl1.bindValue("a", 1);
		
		assertEquals(repl1.getValue("a"), 1);
	}

}
