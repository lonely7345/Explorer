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
package com.stratio.explorer.socket;

import com.stratio.explorer.notebook.Notebook;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public interface IExplorerOperation {

    /**
     * This method execute the NoteBook Opereation.
     * @param conn the websocket.
     * @param notebook the notebook
     * @param messagereceived the message.
     *
     * @throw NotebookOperationException if a exception happens.
     *
     */
    void execute(WebSocket conn, Notebook notebook,  Message messagereceived) throws ExplorerOperationException;
}
