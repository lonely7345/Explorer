package com.stratio.crossdata;

import com.stratio.crossdata.common.result.*;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.utils.CrossdataUtils;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.interpreter.ResultHandler;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.scheduler.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrossdataResultHandler extends ResultHandler implements IDriverResultHandler {

    /**
     * The Log.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CrossdataResultHandler.class);

    public CrossdataInterpreter interpreter;

    public CrossdataResultHandler(CrossdataInterpreter interpreter, Paragraph p) {
        this.interpreter = interpreter;
        this.paragraph = p;
    }

    @Override
    public void processAck(String queryId, QueryStatus status) {
    }

    @Override
    public void processError(Result errorResult) {
        LOG.info("Viewer is processing a error" + ErrorResult.class.cast(errorResult).getErrorMessage());
        paragraph.setReturn(new InterpreterResult(InterpreterResult.Code.SUCCESS,
                ErrorResult.class.cast(errorResult).getErrorMessage()));
        isLastResult = true;
    }

    @Override
    public void processResult(Result result) {
        StringBuilder sb = new StringBuilder();
        sb.append(CrossdataUtils.resultToString(result));
        if (LOG.isDebugEnabled()) {
            LOG.debug("The result class is " + result.getClass());
            LOG.debug("The return is" + sb.toString());
        }
        paragraph.setReturn(new InterpreterResult(InterpreterResult.Code.SUCCESS, sb.toString()));
        if (QueryResult.class.isInstance(result)) {
            QueryResult qr = QueryResult.class.cast(result);
            if (LOG.isDebugEnabled()) {
               LOG.debug("is it the Last Result?" + qr.isLastResultSet());
            }
            isLastResult = qr.isLastResultSet();
            //TODO work around, waiting to crossdata send lastresult correctly
            if (!paragraph.getText().contains("WINDOW")) {
                isLastResult = true;
            }
            if (paragraph.getStatus() == Job.Status.RUNNING) {
                paragraph.setStatus(Job.Status.REFRESH_RESULT);
                paragraph.setStatus(Job.Status.RUNNING);
            }
            if (isLastResult) {
                interpreter.removeHandler(result.getQueryId());
            }
        } else {
            isLastResult = true;
        }
    }

}
