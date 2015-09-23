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
package com.stratio.notebook.socket.notebookOperations;

import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmgomez on 3/09/15.
 */
public class ImportNoteOperation implements com.stratio.notebook.socket.INotebookOperation {

    private static final Logger LOG = LoggerFactory.getLogger(ExportNoteOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        try {
        String path = (String) messagereceived.get("path");
        String[] filePath = path.split("/");
        String filename = filePath[filePath.length-1];
            LOG.debug("##### filename " + filename);
            Note note = notebook.importFromFile(filename, path);
            LOG.debug("##### Noteid " + note.id());
            for(Paragraph p : note.getParagraphs()){
                System.out.println("##### Paragraph: "+p.getText());
            }
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
            new BroadcastNoteListOperation().execute(conn, notebook, messagereceived);
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.IMPORT_INFO).put("info", "Imported successfully"));

            //TODO review this exception without log. Throwable catcg all exceptions.
        } catch (Throwable e) {
            LOG.info("We are catch a IOException when we try to import a note but we continue."+e.getMessage());
            e.printStackTrace();
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.IMPORT_INFO).put("info", e.getMessage()));
        }

    }
}
