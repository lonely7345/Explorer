package com.stratio.notebook.interpreter.mock;

import java.util.Properties;

import com.stratio.notebook.conf.ZeppelinConfiguration;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterFactory;

public class MockInterpreterFactory extends InterpreterFactory {

	public MockInterpreterFactory(ZeppelinConfiguration conf) {
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
