package com.stratio.notebook.interpreter;

public class InterpreterException extends RuntimeException {
	
    public InterpreterException(Throwable e){
		super(e);
	}

	public InterpreterException(String m) {
		super(m);
	}

}
