package com.stratio.crossdata;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import com.nflabs.zeppelin.interpreter.AsyncInterpreterResult;
import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.scheduler.Scheduler;
import com.nflabs.zeppelin.scheduler.SchedulerFactory;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.sh.utils.ConsoleUtils;
import com.stratio.crossdata.utils.CrossdataUtils;

public class CrossdataInterpreter extends Interpreter {

    private final BasicDriver xdDriver;

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
        try {
            connect();
        } catch (ConnectionException e) {
            return new InterpreterResult(InterpreterResult.Code.ERROR, "couldn't connect with Crossdata server");
        }
        StringBuilder sb = new StringBuilder();
        String normalizedSentence = st.trim().toLowerCase();
        if (normalizedSentence.startsWith("clean metadata")) {
            xdDriver.cleanMetadata();
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, "METADATA clean successful");
        } else if (normalizedSentence.startsWith("reset serverdata")) {
            xdDriver.resetServerdata();
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, "SERVER DATA reset successful");
        } else if (normalizedSentence.startsWith("describe connectors")) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                    ConsoleUtils.stringResult(xdDriver.describeConnectors()));
        } else if (normalizedSentence.startsWith("describe connector ")) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                    ConsoleUtils.stringResult(xdDriver.describeConnector(
                            new ConnectorName(
                                    st.toLowerCase().replace("describe connector ", "").replace(";", "").trim()))));
        } else if (normalizedSentence.startsWith("describe system")) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                    ConsoleUtils.stringResult(xdDriver.describeSystem()));
        } else if (normalizedSentence.startsWith("describe datastore ")) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                    ConsoleUtils.stringResult(xdDriver.describeDatastore(new DataStoreName(st.toLowerCase().replace
                            ("describe datastore ", "").replace(";", "").trim()))));
        } else if (normalizedSentence.startsWith("describe catalogs")) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                    ConsoleUtils.stringResult(xdDriver.listCatalogs()));
        } else if (normalizedSentence.startsWith("drop datastore")) {
            try {
                Result resultManifest = xdDriver.dropManifest(CrossdataManifest.TYPE_DATASTORE,
                        st.toLowerCase().replace("drop datastore ", "").replace(";", "").trim());
                String message;
                if (resultManifest.hasError()) {
                    ErrorResult errorResult = (ErrorResult) resultManifest;
                    message = errorResult.getErrorMessage();
                } else {
                    CommandResult commandResult = (CommandResult) resultManifest;
                    message = commandResult.getResult().toString();
                }
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, message);

            } catch (ManifestException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "Couldn't drop");
            }
        } else if (normalizedSentence.startsWith("drop connector")) {
            try {
                Result resultManifest = xdDriver.dropManifest(CrossdataManifest.TYPE_CONNECTOR,
                        st.toLowerCase().replace("drop connector ", "").replace(";", "").trim());
                String message;
                if (resultManifest.hasError()) {
                    ErrorResult errorResult = (ErrorResult) resultManifest;
                    message = errorResult.getErrorMessage();
                } else {
                    CommandResult commandResult = (CommandResult) resultManifest;
                    message = commandResult.getResult().toString();
                }
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, message);

            } catch (ManifestException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "Couldn't drop");
            }
        } else if (normalizedSentence.startsWith("explain plan for")) {
            return new InterpreterResult(InterpreterResult.Code.SUCCESS,
                    ConsoleUtils.stringResult(xdDriver.explainPlan(st.substring("explain plan for".length()))));
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
            } catch (ManifestException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "CrossdataManifest couldn't be parsed");
            } catch (FileNotFoundException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "CrossdataManifest couldn't be accessed \n" +
                        e.toString());
            }
            Result metaResult;

            try {
                metaResult = xdDriver.addManifest(manifest);
            } catch (ManifestException e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, "CrossdataManifest couldn't added to " +
                        "server");
            }
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, CrossdataUtils.resultToString
                    (metaResult));

        } else {
            try {
                CrossdataResultHandler callback = new CrossdataResultHandler();
                xdDriver.asyncExecuteQuery(st, callback);
                return new AsyncInterpreterResult(InterpreterResult.Code.SUCCESS, callback);
            } catch (Exception e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
            }
        }
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

}
