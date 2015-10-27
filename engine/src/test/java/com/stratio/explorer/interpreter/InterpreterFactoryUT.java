package com.stratio.explorer.interpreter;

import static org.junit.Assert.*;

import java.util.Properties;

import com.stratio.explorer.conf.ConstantsFolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stratio.explorer.conf.ExplorerConfiguration;

//TODO : this test is failed
public class InterpreterFactoryUT {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasic() {
		InterpreterFactory factory = new InterpreterFactory(ExplorerConfiguration.create(ConstantsFolder.CT_NAME_FILE_INTERPRETERS_CONFIGURE));
		Interpreter repl1 = factory.createRepl("mock", "com.stratio.explorer.interpreter.mock.MockInterpreter", new Properties());
		repl1.bindValue("a", 1);
		
		assertEquals(repl1.getValue("a"), 1);
	}

}
