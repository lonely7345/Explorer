package com.stratio.notebook.notebook.cassandra;

import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterDriver;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.notebook.cassandra.exceptions.CassandraInterpreterException;

import com.stratio.notebook.notebook.cassandra.operations.CQLOperation;
import com.stratio.notebook.notebook.cassandra.operations.OperationFactory;

import java.util.List;


public class CassandraInterpreter extends Interpreter {


    static {
        Interpreter.register("cql", CassandraInterpreter.class.getName());
    }

    private OperationFactory operationFactory ;


    public CassandraInterpreter(InterpreterDriver driver){
        operationFactory = new OperationFactory(driver);
    }

    @Override public void open() {

    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {
        String first = st.split(" ")[0];
        CQLOperation cqlOperation = operationFactory.operation(first);
        try {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,cqlOperation.execute(st));
        }catch (CassandraInterpreterException e){
            return new InterpreterResult(InterpreterResult.Code.ERROR, e.getErrorMessage());
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
