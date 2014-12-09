package com.stratio.crossdata;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.scheduler.Scheduler;
import com.nflabs.zeppelin.scheduler.SchedulerFactory;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.metadata.structures.ColumnMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.result.StorageResult;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.sh.utils.ConsoleUtils;

public class CrossdataInterpreter extends Interpreter {

    private final BasicDriver xdDriver;
    private boolean endStream;

    static {
        Interpreter.register("xd", CrossdataInterpreter.class.getName());
    }

    public CrossdataInterpreter(Properties property) {
        super(property);
        //Driver that connects to the CROSSDATA servers.
        xdDriver = new BasicDriver();
        xdDriver.setUserName("USER");
    }

    @Override public void open() {

    }

    @Override public void close() {

    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {
        Result result;

        //if stream flag check endStream flag and return result sacado del map
        try {
            connect();
        } catch (ConnectionException e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, "couldn't connect with Crossdata server");
        }
        StringBuilder sb = new StringBuilder();
        String normalizedSentence = st.trim().toLowerCase();
        if (normalizedSentence.startsWith("streaming")) { //streaming spike
            while (!endStream){
                //check results UNDEFINED
                //bind values -> mete el objeto del result en un map en memoria
                //interpret(string flag)
                //Console.setOut(new PrintStream(System.out.printf("molo mucho")));
            }
            endStream=false;
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,"STREAMING QUERY ended");
        }else if (normalizedSentence.startsWith("reset metadata")) {
            xdDriver.resetMetadata();
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,"METADATA reset successful");
        } else if (normalizedSentence.startsWith("add connector") || normalizedSentence
                .startsWith("add datastore")) {
            // Get manifest type
            String[] tokens = st.split(" ");
            if (tokens.length != 3) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "Invalid ADD syntax");
            }

            int typeManifest;
            if (tokens[1].equalsIgnoreCase("datastore")) {
                typeManifest = CrossdataManifest.TYPE_DATASTORE;
            } else if (tokens[1].equalsIgnoreCase("connector")) {
                typeManifest = CrossdataManifest.TYPE_CONNECTOR;
            } else {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "Unknown type: " + tokens[1]);
            }

            // Create CrossdataManifest object from XML file
            CrossdataManifest manifest;
            try {
                manifest = ConsoleUtils.parseFromXmlToManifest(typeManifest,
                        tokens[2].replace(";", "").replace("\"", "").replace("'", "").trim());
            } catch (ManifestException e)  {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "CrossdataManifest couldn't be parsed");
            } catch ( FileNotFoundException e){
                return new InterpreterResult(InterpreterResult.Code.ERROR, "CrossdataManifest couldn't be accessed \n"+
                        e.toString());
            }
            Result metaResult;

            try {
                metaResult = xdDriver.addManifest(manifest);
            } catch (ManifestException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "CrossdataManifest couldn't added to " +
                        "server");
            }
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, resultToString
                    (metaResult));

        } else {
            try {
                result = xdDriver.executeQuery(st);
                sb.append(resultToString(result));
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, sb.toString());
            } catch (UnsupportedException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
            } catch (Exception e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
            }
        }
    }

    @Override public void cancel() {
        endStream=true;
    }

    @Override public void bindValue(String name, Object o) {
    }

    @Override public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override public int getProgress() {
        return 0;
    }
    @Override
    public Scheduler getScheduler() {
        return SchedulerFactory.singleton().createOrGetParallelScheduler("interpreter_" + this.hashCode(), 100);
    }

    @Override public List<String> completion(String buf, int cursor) {
        return null;
    }

    /**
     * Remove the {@link com.stratio.crossdata.common.result.IResultHandler} associated with a query.
     *
     * @param queryId The query identifier.
     */
    protected void removeResultsHandler(String queryId) {
        xdDriver.removeResultHandler(queryId);
    }

    /**
     * Establish the connection with the Crossdata servers.
     *
     * @return Whether the connection has been successfully established.
     */
    public void connect() throws ConnectionException {
        xdDriver.connect(xdDriver.getUserName());
    }

    public String resultToString(Result result) {
        if (ErrorResult.class.isInstance(result)) {
            return ErrorResult.class.cast(result).getErrorMessage();
        }
        if (result instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) result;
            return queryResultToString(queryResult);
        } else if (result instanceof CommandResult) {
            CommandResult commandResult = (CommandResult) result;
            return String.class.cast(commandResult.getResult());
        } else if (result instanceof ConnectResult) {
            ConnectResult connectResult = (ConnectResult) result;
            return String.valueOf("Connected with SessionId=" + connectResult.getSessionId());
        } else if (result instanceof MetadataResult) {
            MetadataResult metadataResult = (MetadataResult) result;
            return metadataResult.toString();
        } else if (result instanceof StorageResult) {
            StorageResult storageResult = (StorageResult) result;
            return storageResult.toString();
        } else {
            return "Unknown result";
        }
    }

    public String queryResultToString(QueryResult result) {
        StringBuilder sb = new StringBuilder();
        if (result.getResultSet().isEmpty()) {
            return "%text OK";
        }

        ResultSet resultSet = null;
        resultSet = result.getResultSet();
        sb.append("%table ");

        for (ColumnMetadata c : resultSet.getColumnMetadata()) {

            sb.append(c.getColumnNameToShow()).append("\t");
        }
        sb.replace(sb.length() - 1, sb.length(), "\n");

        for (Row r : resultSet.getRows()) {
            for (Map.Entry<String, Cell> c : r.getCells().entrySet()) {
                sb.append(c.getValue()).append("\t");
            }
            sb.replace(sb.length() - 1, sb.length(), "\n");
        }

        return sb.toString();
    }

}
