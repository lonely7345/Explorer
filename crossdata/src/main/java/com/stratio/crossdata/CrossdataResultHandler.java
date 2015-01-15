package com.stratio.crossdata;

import com.nflabs.zeppelin.interpreter.InterpreterResult;
import com.nflabs.zeppelin.interpreter.ResultHandler;
import com.nflabs.zeppelin.notebook.Paragraph;
import com.nflabs.zeppelin.scheduler.Job;
import com.stratio.crossdata.common.result.IResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.utils.CrossdataUtils;

public class CrossdataResultHandler extends ResultHandler implements IResultHandler {

    public CrossdataResultHandler() {
        isLastResult = true;
    }

    public CrossdataResultHandler(Paragraph p) {
        paragraph = p;
        isLastResult = true;
    }

    @Override public void processAck(String queryId, QueryStatus status) {

    }

    @Override public void processError(Result errorResult) {

    }

    @Override public void processResult(Result result) {
        StringBuilder sb = new StringBuilder();
        sb.append(CrossdataUtils.resultToString(result));
        paragraph.setReturn(new InterpreterResult(InterpreterResult.Code.SUCCESS, sb.toString()));
        if (QueryResult.class.isInstance(result)) {
            QueryResult qr = QueryResult.class.cast(result);
            isLastResult = qr.isLastResultSet();
            System.out.println("CrossdataResultHandler - is last result (QueryResult) " + qr.isLastResultSet());
        }

        System.out.println("CrossdataResultHandler - is last result " + isLastResult);

    }

}
