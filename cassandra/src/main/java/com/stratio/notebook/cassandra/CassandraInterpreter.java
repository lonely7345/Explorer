package com.stratio.notebook.cassandra;


import com.stratio.notebook.cassandra.drivers.CassandraDriver;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterDriver;
import com.stratio.notebook.interpreter.InterpreterResult;

import java.util.List;


public class CassandraInterpreter extends Interpreter {


    static {
        Interpreter.register("cql", CassandraInterpreter.class.getName());
    }

    private InterpreterDriver driver;

    public CassandraInterpreter(InterpreterDriver driver){
        this.driver = driver;
    }


    @Override public void open() {

    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {
        try {
            driver.connect();
            driver.executeCommand(st);
            return new InterpreterResult(InterpreterResult.Code.ERROR);

        }catch (ConnectionException e){
            return new InterpreterResult(InterpreterResult.Code.ERROR,e.getErrorMessage());

        }catch (CassandraInterpreterException e){
            return new InterpreterResult(InterpreterResult.Code.ERROR,e.getErrorMessage());
        }

    }

    @Override public void cancel() {

    }

    @Override public void bindValue(String name, Object o) {

    }

    @Override public FormType getFormType() {
        return null;
    }

    @Override public int getProgress() {
        return 0;
    }

    @Override public List<String> completion(String buf, int cursor) {
        return null;
    }
}
