package com.stratio.notebook.interpreter.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.scheduler.Scheduler;
import com.stratio.notebook.scheduler.SchedulerFactory;

public class MockInterpreter extends Interpreter {
	
	public MockInterpreter(Properties property) {
		super(property);
	}

	static Map<String, Object> vars = new HashMap<String, Object>();

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public Object getValue(String name) {
		return vars.get(name);
	}

	@Override
	public InterpreterResult interpret(String st) {
		return new InterpreterResult(InterpreterResult.Code.SUCCESS, st);
	}

	@Override
	public void bindValue(String name, Object o) {
		vars.put(name, o);
	}

	@Override
	public void cancel() {
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
		return SchedulerFactory.singleton().createOrGetFIFOScheduler("test_"+this.hashCode());
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		return null;
	}

}
