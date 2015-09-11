package com.stratio.notebook.mongodb;

import com.mongodb.MongoClient;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.mongodb.command.CommandDecider;
import com.stratio.notebook.mongodb.command.IMongoCommandExecutor;
import com.stratio.notebook.mongodb.command.MongoCommandExecutorShowDBS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;



/**
 * Created by jmgomez on 10/09/15.
 * This Class is the mongodb interpretor to Notebook.
 */
public class MongoDBInterpreter  extends Interpreter {


    static {
        Interpreter.register("mdb", MongoDBInterpreter.class.getName());
    }



    /**
     * The Log.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MongoDBInterpreter.class);

    /**
     * The mongoClient.
     */
    private MongoClient mongoClient;


    public MongoDBInterpreter(Properties property) {
        super(property);

    }

    @Override
    public void open() {
        synchronized(this) {
            if (mongoClient == null) {
                mongoClient = new MongoClient();
                LOG.info("Notebook has created a mongo connection.");
            }
        }
    }

    @Override
    public void close() {
        synchronized(this) {
            if (mongoClient != null) {
                mongoClient.close();
                mongoClient = null;
                LOG.info("Notebook has close a mongo connection.");

            }
        }
    }

    @Override
    public Object getValue(String name) {

        return null;
    }

    @Override
    public InterpreterResult interpret(String st) {
        if (LOG.isDebugEnabled()){
            LOG.debug("Start MongoInterpreter with "+st);
        }
        StringBuilder sb = new StringBuilder("");
        sb.append(CommandDecider.decideCommand(st, mongoClient).execute(st));


        return new InterpreterResult(InterpreterResult.Code.SUCCESS,sb.toString());
    }

    @Override
    public void cancel() {

    }

    @Override
    public void bindValue(String name, Object o) {
    }

    @Override
    public FormType getFormType() {
        return null;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public List<String> completion(String buf, int cursor) {
        return null;
    }
}
