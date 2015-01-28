package com.stratio.crossdata;

import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.interpreter.ResultHandler;
import com.nflabs.zeppelin.notebook.Paragraph;
import com.nflabs.zeppelin.scheduler.Job;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.utils.CrossdataUtils;

public class CrossdataResultHandler extends ResultHandler implements IDriverResultHandler {

    public CrossdataInterpreter interpreter;

    public CrossdataResultHandler(CrossdataInterpreter interpreter, Paragraph p) {
        this.interpreter = interpreter;
        this.paragraph = p;
    }

    @Override public void processAck(String queryId, QueryStatus status) {
    }

    @Override public void processError(Result errorResult) {
        System.out.println(
                "*****[CrossdataResultHandler]ErrorResult ->" + ErrorResult.class.cast(errorResult).getErrorMessage());
        paragraph.setReturn(new InterpreterResult(InterpreterResult.Code.SUCCESS,
                ErrorResult.class.cast(errorResult).getErrorMessage()));
        isLastResult = true;
    }

    @Override public void processResult(Result result) {
        StringBuilder sb = new StringBuilder();
        sb.append(CrossdataUtils.resultToString(result));
        System.out.println("*****[CrossdataResultHandler]ProcessResult "+ result.getClass());
        System.out.println("*****[CrossdataResultHandler]ProcessResult ->" + sb.toString());
        paragraph.setReturn(new InterpreterResult(InterpreterResult.Code.SUCCESS, sb.toString()));
        if (QueryResult.class.isInstance(result)) {
            QueryResult qr = QueryResult.class.cast(result);
            System.out.println("*****[CrossdataResultHandler]ProcessResult -> isLastResult = "+qr.isLastResultSet());
            isLastResult = qr.isLastResultSet();
            //ñapa hasta que en crossdata venga bien el lastresultset
            if(!paragraph.getText().contains("WINDOW")){
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
