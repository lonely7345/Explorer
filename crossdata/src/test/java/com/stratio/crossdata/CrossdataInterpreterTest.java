/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.junit.Test;

import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.driver.DriverConnection;
import com.stratio.crossdata.exception.AsyncQueryTimeoutException;

public class CrossdataInterpreterTest extends AbstractInterpreterTest {

    private CrossdataInterpreter interpreter = new CrossdataInterpreter(new Properties());

    @Test(expected = AsyncQueryTimeoutException.class, timeout = 80000L)
    public void executeAsyncRawQueryShouldReturnException() throws Exception {
        DriverConnection mockDriver = mock(DriverConnection.class);
        CrossdataResultHandler mockCallback = mock(CrossdataResultHandler.class);

        when(mockDriver.executeAsyncRawQuery("", mockCallback))
                .thenReturn(InProgressResult.createInProgressResult("id"));
        when(mockCallback.getQueryStatus("id")).thenReturn(CrossdataResultHandler.AsyncQueryStatus.RUNNING);

        setFieldTo(interpreter, "xdConnection", mockDriver);
        Method methodToTest = getMethodToTest(interpreter, "executeAsyncRawQuery", String.class,
                CrossdataResultHandler.class);
        try {
            methodToTest.invoke(interpreter, "", mockCallback);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AsyncQueryTimeoutException) {
                throw new AsyncQueryTimeoutException();
            } else {
                throw e;
            }
        }
    }

    @Test
    public void executeAsyncRawQueryShouldReturnOk() throws Exception {
        DriverConnection mockDriver = mock(DriverConnection.class);
        CrossdataResultHandler mockCallback = mock(CrossdataResultHandler.class);

        when(mockDriver.executeAsyncRawQuery("", mockCallback))
                .thenReturn(InProgressResult.createInProgressResult("id"));
        when(mockCallback.getQueryStatus("id")).thenReturn(CrossdataResultHandler.AsyncQueryStatus.DONE);

        setFieldTo(interpreter, "xdConnection", mockDriver);
        Method methodToTest = getMethodToTest(interpreter, "executeAsyncRawQuery", String.class,
                CrossdataResultHandler.class);
        try {
            methodToTest.invoke(interpreter, "", mockCallback);
        } catch (Exception e) {
            throw e;
        }

    }
}
