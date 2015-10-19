/*
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

package com.stratio.notebook.socket.notebookOperations;


import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.scheduler.Job;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import org.java_websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;


import static com.stratio.notebook.socket.notebookOperations.MocksToOperations.mockSocket;
import static com.stratio.notebook.socket.notebookOperations.MocksToOperations.mockNotebook;
import static com.stratio.notebook.socket.notebookOperations.MocksToOperations.mockMessage;


public class CancelParagraphOperationTest {

    private CancelParagraphOperation operation;


    @Before
    public void setUp(){
        operation = new CancelParagraphOperation();
    }



    @Test
    public void whenCallExecuteAndIdIsNull(){
        operation.execute(mockSocket(),mockNotebook(false),mockMessage(null));
    }


    @Test
    public void whenCallExecuteAndIdIsempty(){
        operation.execute(mockSocket(),mockNotebook(false),mockMessage(""));
    }


    @Test
    public void whenCallExecuteAndIdnotempty(){
        operation.execute(mockSocket(),mockNotebook(true),mockMessage("anyId"));
    }

}
