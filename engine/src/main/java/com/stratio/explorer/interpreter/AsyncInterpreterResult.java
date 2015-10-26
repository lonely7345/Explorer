package com.stratio.explorer.interpreter;

public class AsyncInterpreterResult extends InterpreterResult {
    private ResultHandler resultHandler;

    public AsyncInterpreterResult(Code code, ResultHandler resultHandler) {
        super(code);
        this.resultHandler = resultHandler;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }
}
