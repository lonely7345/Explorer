package com.stratio.crossdata;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.nflabs.zeppelin.interpreter.AsyncInterpreterResult;
import com.nflabs.zeppelin.interpreter.Interpreter;
import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.notebook.Paragraph;
import com.nflabs.zeppelin.scheduler.Job;
import com.nflabs.zeppelin.scheduler.Scheduler;
import com.nflabs.zeppelin.scheduler.SchedulerFactory;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.driver.DriverConnection;
import com.stratio.crossdata.utils.CrossdataUtils;

public class CrossdataInterpreter extends Interpreter {

    private BasicDriver xdDriver;
    private DriverConnection xdConnection;
    private Paragraph paragraph;
    private String sessionId;
    private HashMap<String, String> queryIds;

    static {
        Interpreter.register("xdql", CrossdataInterpreter.class.getName());
    }

    public CrossdataInterpreter(Properties property) {
        super(property);
        //Driver that connects to the CROSSDATA servers.
        xdDriver = new BasicDriver();
        xdDriver.setUserName("USER");
        queryIds= new HashMap<String,String>();
    }

    @Override public void open() {
        if(xdDriver == null){
            xdDriver = new BasicDriver();
            xdDriver.setUserName("USER");
        }
        try {
            connect();
            System.out.println("Crossdata's driver connected");
        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override public void close() {
        xdDriver.close();
        xdDriver = null;
    }

    @Override public Object getValue(String name) {
        return null;
    }

    @Override public InterpreterResult interpret(String st) {
        Result result;
        String[] commands = st.split(";");
        sessionId="sessionId";

        if (commands.length > 1) { //multiline command
            StringBuilder sb = new StringBuilder();

            for (String i : commands) {
                try {
                    String normalized = i.replaceAll("\\s+", " ").replaceAll("(\\r|\\n)", "").trim()+";";
                    System.out.println("*****[CrossdataInterpreter]interpret multiline query -> "+normalized);
                    result = xdConnection.executeRawQuery(normalized);

                    sb.append(CrossdataUtils.resultToString(result)).append(
                            System.getProperty("line.separator")).append(System.getProperty("line.separator"));
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

    @Override public void cancel() {
        assert paragraph.getResult() != null: paragraph;
        String queryId = queryIds.get(paragraph.getId());
        xdConnection.stopProcess(queryId);
        paragraph.setStatus(Job.Status.ABORT);
        paragraph.setListener(null);
        paragraph = null;
    }

    @Override public void bindValue(String name, Object o) {
        if (name.equals("paragraph") && Paragraph.class.isInstance(o)) {
            this.paragraph = Paragraph.class.cast(o);
        }
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
     * Establish the connection with the Crossdata servers.
     *
     * @return Whether the connection has been successfully established.
     */
    public void connect() throws ConnectionException {
       xdConnection = xdDriver.connect(xdDriver.getUserName(), "PASSWORD"); //TODO: get user password from front
    }

}
