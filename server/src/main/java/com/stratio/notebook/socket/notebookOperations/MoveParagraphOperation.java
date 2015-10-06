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
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import com.stratio.notebook.socket.NotebookOperationException;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jmgomez on 3/09/15.
 */
public class MoveParagraphOperation implements com.stratio.notebook.socket.INotebookOperation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNoteOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws NotebookOperationException {
        try{
        final String paragraphId = (String) messagereceived.get("id");
        if (paragraphId != null) {
            final int newIndex = (int) Double.parseDouble(messagereceived.get("index").toString());
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            note.moveParagraph(paragraphId, newIndex);
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
        }
    }catch(IOException ioe){
        String msg = "A exception happens in when we are trying to move a paragraph."+ioe.getMessage();
        LOG.error(msg);
        throw new NotebookOperationException(msg,ioe);
    }
    }
}
