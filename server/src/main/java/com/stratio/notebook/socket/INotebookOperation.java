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
package com.stratio.notebook.socket;

import com.stratio.notebook.notebook.Notebook;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public interface INotebookOperation {

    /**
     * This method execute the NoteBook Opereation.
     * @param conn the websocket.
     * @param notebook the notebook
     * @param messagereceived the message.
     *
     * @throw NotebookOperationException if a exception happens.
     *
     */
    void execute(WebSocket conn, Notebook notebook,  Message messagereceived) throws NotebookOperationException;
}
