package com.stratio.explorer.interpreter.mock;

import java.util.Properties;

import com.stratio.explorer.conf.ExplorerConfiguration;
import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterFactory;

public class MockInterpreterFactory extends InterpreterFactory {

	public MockInterpreterFactory(ExplorerConfiguration conf) {
		super(conf);
	}
	
	public Interpreter createRepl(String replName, Properties properties) {
		if("MockRepl1".equals(replName) || replName==null) {
			return new MockInterpreter1(properties);
		} else if("MockRepl2".equals(replName)) {
			return new MockInterpreter2(properties);
		} else {
			return new MockInterpreter1(properties);
		}
	}
}
