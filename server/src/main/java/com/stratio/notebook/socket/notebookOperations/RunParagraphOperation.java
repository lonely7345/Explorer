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

import com.google.common.base.Strings;
import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import com.stratio.notebook.socket.NotebookOperationException;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jmgomez on 3/09/15.
 */
public class RunParagraphOperation implements com.stratio.notebook.socket.INotebookOperation {

    private static final Logger LOG = LoggerFactory.getLogger(RunParagraphOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        System.out.println("ACABA DE ENTRAR POR AQUÍ");
        try {
            final String paragraphId = (String) messagereceived.get("id");
            if (paragraphId != null) {
                final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
                Paragraph p = note.getParagraph(paragraphId);
                String text = (String) messagereceived.get("paragraph");
                p.setText(text);
                p.setTitle((String) messagereceived.get("title"));
                Map<String, Object> params = (Map<String, Object>) messagereceived.get("params");
                p.settings.setParams(params);
                Map<String, Object> config = (Map<String, Object>) messagereceived.get("config");
                config.put("tableHide", false);
                p.setConfig(config);

                // if it's the last paragraph, let's add a new one
                boolean isTheLastParagraph = note.getLastParagraph().getId().equals(p.getId());
                if (!Strings.isNullOrEmpty(text) && isTheLastParagraph) {
                    note.addParagraph();
                }
                note.persist();
                ConnectionManager.getInstance().broadcastNote(note);

                note.run(paragraphId);
            }
        }catch(IOException ioe){
            String msg = "A exception happens in when we are trying to execute paragraph."+ioe.getMessage();
            LOG.error(msg);
            new NotebookOperationException(msg,ioe);
        }

    }
}
