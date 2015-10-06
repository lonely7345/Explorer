package com.stratio.crossdata;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.driver.DriverConnection;
import com.stratio.crossdata.utils.CrossdataUtils;
import com.stratio.notebook.interpreter.AsyncInterpreterResult;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.scheduler.Job;
import com.stratio.notebook.scheduler.Scheduler;
import com.stratio.notebook.scheduler.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class CrossdataInterpreter extends Interpreter {

    /**
     * The Log.
     */
    private Logger logger = LoggerFactory.getLogger(CrossdataInterpreter.class);

    static {
        Interpreter.register("xdql", CrossdataInterpreter.class.getName());
    }

    private BasicDriver xdDriver;
    private DriverConnection xdConnection;
    private Paragraph paragraph;
    private String sessionId;
    private HashMap<String, String> queryIds;
    private boolean driverConnected;

    public CrossdataInterpreter(Properties property) {
        super(property);
        //Driver that connects to the CROSSDATA servers.
        xdDriver = new BasicDriver();
        xdDriver.setUserName("USER");
        queryIds = new HashMap<String, String>();
        driverConnected = false;
    }

    @Override
    public void open() {
        if (xdDriver == null) {
            xdDriver = new BasicDriver();
            xdDriver.setUserName("USER");
        }
        try {
            connect();
            logger.info("Crossdata's driver connected");
            driverConnected = true;
        } catch (ConnectionException e) {
            logger.info("A error happens when we are trying to connect to crossdata"+ e.getMessage());
            driverConnected = false;
        }

    }

    @Override
    public void close() {
        xdDriver.close();
        xdDriver = null;
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public InterpreterResult interpret(String st) {
        Result result;
        String[] commands = st.split(";");
        sessionId = "sessionId";
        if (!driverConnected) {
            open();
            if (!driverConnected) return new InterpreterResult(InterpreterResult.Code.ERROR, "Couldn't connect to "
                    + "Crossdata's server. Not found answer");
        }

        if (commands.length > 1) { //multiline command
            StringBuilder sb = new StringBuilder();

            for (String i : commands) {
                try {
                    String normalized = i.replaceAll("\\s+", " ").replaceAll("(\\r|\\n)", "").trim() + ";";
                    logger.info("*****[CrossdataInterpreter]interpret multiline query -> " + normalized);
                    result = xdConnection.executeRawQuery(normalized);

                    sb.append(CrossdataUtils.resultToString(result)).append(
                            System.lineSeparator()).append(System.lineSeparator());
                } catch (Exception e) {
                    return new InterpreterResult(InterpreterResult.Code.ERROR, e.getLocalizedMessage());
                }
            }
            return new InterpreterResult(InterpreterResult.Code.SUCCESS, sb.toString());

        } else {

            CrossdataResultHandler callback = new CrossdataResultHandler(this, paragraph);

            try {
                result = xdConnection.executeAsyncRawQuery(st.replaceAll("\\s+", " ").trim(), callback);
                if (ErrorResult.class.isInstance(result)) {
                    return new InterpreterResult(InterpreterResult.Code.ERROR,
                            ErrorResult.class.cast(result).getErrorMessage());
                } else if (InProgressResult.class.isInstance(result)) {
                    queryIds.put(paragraph.getId(), result.getQueryId());
                    return new AsyncInterpreterResult(InterpreterResult.Code.SUCCESS, callback);
                }
                return new InterpreterResult(InterpreterResult.Code.SUCCESS, CrossdataUtils.resultToString(result));

            } catch (Exception e) {
                return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
            }
        }

    }

    public void removeHandler(String queryId) {
        xdConnection.removeResultHandler(queryId);
    }

    @Override
    public void cancel() {
        assert paragraph.getResult() != null : paragraph;
        String queryId = queryIds.get(paragraph.getId());
        xdConnection.stopProcess(queryId);
        paragraph.setStatus(Job.Status.ABORT);
        paragraph.setListener(null);
        paragraph = null;
    }

    @Override
    public void bindValue(String name, Object o) {
        if (name.equals("paragraph") && Paragraph.class.isInstance(o)) {
            this.paragraph = Paragraph.class.cast(o);
        }
    }

    @Override
    public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public Scheduler getScheduler() {
        return SchedulerFactory.singleton().createOrGetParallelScheduler("interpreter_" + this.hashCode(), 100);
    }

    @Override
    public List<String> completion(String buf, int cursor) {
        return null;
    }

    /**
     * Establish the connection with the Crossdata servers.
     *
     * @return Whether the connection has been successfully established.
     */
    public void connect() throws ConnectionException {
        xdConnection = xdDriver.connect(xdDriver.getUserName(), "PASSWORD"); //TODO: get user password from front
    }

}
