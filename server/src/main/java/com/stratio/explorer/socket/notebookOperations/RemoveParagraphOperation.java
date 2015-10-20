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
package com.stratio.explorer.socket.notebookOperations;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.Message;
import com.stratio.explorer.socket.NotebookOperationException;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jmgomez on 3/09/15.
 */
public class RemoveParagraphOperation implements com.stratio.explorer.socket.INotebookOperation {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveParagraphOperation.class);


    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws NotebookOperationException {
    try{
        final String paragraphId = (String) messagereceived.get("id");
        if (paragraphId != null) {
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            /** We dont want to remove the last paragraph */
            if (!note.isLastParagraph(paragraphId)) {
                note.removeParagraph(paragraphId);
                note.persist();
                ConnectionManager.getInstance().broadcastNote(note);
            }
        }
    }catch(IOException ioe){
        String msg = "A exception happens in when we are trying to remove a paragraph."+ioe.getMessage();
        LOG.error(msg);
        throw  new NotebookOperationException(msg,ioe);
        }
    }
}
