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
package com.stratio.explorer.socket.explorerOperations;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import com.stratio.explorer.socket.ExplorerOperationException;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jmgomez on 3/09/15.
 */
public class SplitIntoParagraphsOperation implements IExplorerOperation {
    private static final Logger LOG = LoggerFactory.getLogger(SplitIntoParagraphsOperation.class);


    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        try{
            final int index = (int) Double.parseDouble(messagereceived.get("index").toString());
            final String script = messagereceived.get("paragraph").toString();
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));

            String[] paragraphs = script.split(";");

            note.removeParagraph(String.class.cast(messagereceived.get("id")));
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);

            for (int i = 0; i < paragraphs.length; i++) {
                note.insertParagraph(index + i - 1,
                        paragraphs[i].replaceAll("(\\r|\\n)", "").concat(";"));
                note.persist();
                ConnectionManager.getInstance().broadcastNote(note);

            }
        }catch(IOException ioe){
            String msg = "A exception happens in when we are trying to split into paragraph a note."+ioe.getMessage();
            LOG.error(msg);
            new ExplorerOperationException(msg,ioe);
        }
    }
}
