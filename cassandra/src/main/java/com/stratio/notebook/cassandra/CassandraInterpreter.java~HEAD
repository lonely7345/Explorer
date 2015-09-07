package com.stratio.notebook.cassandra;

import com.stratio.notebook.cassandra.dto.DataTableDTO;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.operations.CQLExecutor;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;

import java.util.List;


public class CassandraInterpreter extends Interpreter {


    static {
        Interpreter.register("cql", CassandraInterpreter.class.getName());
    }

    public CassandraInterpreter(){

        super(null); //TODO : DEBERÉ DE ELIMINAR ESTE DE AQUÍ

    }


    @Override public void open() {

    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }


    @Override public InterpreterResult interpret(String st) {
        InterpreterResult.Code code = InterpreterResult.Code.SUCCESS;
        String message;
        try {
            CQLExecutor executor = new CQLExecutor();
            message = new DataTableDTO().toDTO(executor.execute(st));

        }catch (ConnectionException | CassandraInterpreterException e){
            code =InterpreterResult.Code.ERROR;
            message = e.getMessage();
        }
        return new InterpreterResult(code,message);

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
