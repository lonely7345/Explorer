package com.stratio.notebook.cassandra;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.dto.TableDTO;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.gateways.CassandraDriver;
import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.notebook.cassandra.operations.CQLExecutor;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class CassandraInterpreter extends Interpreter {

    Logger logger = LoggerFactory.getLogger(Interpreter.class);
    static {
        Interpreter.register("cql", CassandraInterpreter.class.getName());
    }

    public CassandraInterpreter(Properties properties){

        super(properties);

        Properties properties1 = new Properties();
        properties1.setProperty(StringConstants.HOST, "127.0.0.1");
        properties1.setProperty(StringConstants.PORT, "9042");
        CassandraInterpreterGateways.commandDriver = new CassandraDriver(properties1);

    }


    @Override public void open() {
       // CassandraInterpreterGateways.commandDriver.connect();
    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }


    @Override public InterpreterResult interpret(String st) {
        InterpreterResult.Code code = InterpreterResult.Code.SUCCESS;
        String message="";
        try {
            CQLExecutor executor = new CQLExecutor();
            message += new TableDTO().toDTO(executor.execute(st));

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
        return FormType.SIMPLE;
    }

    @Override public int getProgress() {
        return 0;
    }

    @Override public List<String> completion(String buf, int cursor) {
        return new ArrayList<>();
    }
}
