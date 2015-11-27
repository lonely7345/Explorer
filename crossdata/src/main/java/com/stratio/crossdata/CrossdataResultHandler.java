/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.utils.CrossdataUtils;
import com.stratio.explorer.interpreter.InterpreterResult;
import com.stratio.explorer.interpreter.ResultHandler;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.scheduler.Job;

public class CrossdataResultHandler extends ResultHandler implements IDriverResultHandler {

    private final HashMap<String, AsyncQueryStatus> queryStatus;

    enum AsyncQueryStatus {
        RUNNING,
        ERROR,
        DONE
    }

    /**
     * The Log.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CrossdataResultHandler.class);

    public CrossdataInterpreter interpreter;

    public CrossdataResultHandler(CrossdataInterpreter interpreter, Paragraph p) {
        this.interpreter = interpreter;
        this.paragraph = p;
        this.queryStatus = new HashMap<String, AsyncQueryStatus>();
    }

    @Override
    public void processAck(String queryId, QueryStatus status) {
    }

    @Override
    public void processError(Result errorResult) {
        LOG.info("Viewer is processing a error" + ErrorResult.class.cast(errorResult).getErrorMessage());
        paragraph.setReturn(new InterpreterResult(InterpreterResult.Code.SUCCESS,
                ErrorResult.class.cast(errorResult).getErrorMessage()));
        updateQueryStatus(errorResult.getQueryId(), AsyncQueryStatus.ERROR);

        isLastResult = true;
    }

    public void updateQueryStatus(String queryId, AsyncQueryStatus status) {
        this.queryStatus.put(queryId, status);
    }

    public AsyncQueryStatus getQueryStatus(String queryId) {
        return queryStatus.get(queryId);
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
        updateQueryStatus(result.getQueryId(),AsyncQueryStatus.DONE);
    }

}
