/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
/**
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stratio.crossdata;

import com.stratio.crossdata.common.result.*;
import com.stratio.crossdata.utils.CrossdataUtils;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.interpreter.ResultHandler;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.scheduler.Job;
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
