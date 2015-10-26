package com.stratio.explorer.cassandra;

import com.stratio.explorer.cassandra.dto.TableDTO;
import com.stratio.explorer.cassandra.gateways.CassandraDriver;
import com.stratio.explorer.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.explorer.cassandra.gateways.CassandraSession;
import com.stratio.explorer.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.explorer.cassandra.exceptions.ConnectionException;
import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.cassandra.operations.CQLExecutor;
import com.stratio.explorer.exceptions.FolderNotFoundException;
import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.reader.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


//TODO : When test of coneccion was ended then change this class an test
public class CassandraInterpreter extends Interpreter {


    private Logger logger = LoggerFactory.getLogger(CassandraInterpreter.class);


    static {
        Interpreter.register("cql", CassandraInterpreter.class.getName());
    }

    public CassandraInterpreter(Properties properties){

        super(properties);
        CassandraInterpreterGateways.commandDriver = new CassandraDriver(new CassandraSession());

    }


    //TODO : Thos method only call firt time , this must be removed
    @Override public void open() {
        try {
            Connector connector = CassandraInterpreterGateways.commandDriver.getConnector();
            connector.loadConfiguration(new PropertiesReader().readConfigFrom("cassandra"));
        }catch (ConnectionException e){
            logger.error("Cassandra database not avalaible " + e.getMessage());
        }
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
            Connector connector = CassandraInterpreterGateways.commandDriver.getConnector();
            connector.loadConfiguration(new PropertiesReader().readConfigFrom("cassandra"));
            CQLExecutor executor = new CQLExecutor();
            message += new TableDTO().toDTO(executor.execute(st));

        }catch (ConnectionException | CassandraInterpreterException e){
            code =InterpreterResult.Code.ERROR;
            message = e.getMessage();
        }catch (FolderNotFoundException e){
            code =InterpreterResult.Code.ERROR;
            message = e.getMessage();
        }catch (NotPropertyFoundException e){
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