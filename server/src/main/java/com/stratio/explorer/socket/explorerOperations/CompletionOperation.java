/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import com.stratio.explorer.socket.Serializer;
import org.java_websocket.WebSocket;

import java.util.List;

/**
 * Created by jmgomez on 3/09/15.
 */
public class CompletionOperation implements IExplorerOperation {
    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        String paragraphId = (String) messagereceived.get("id");
        String buffer = (String) messagereceived.get("buf");
        int cursor = (int) Double.parseDouble(messagereceived.get("cursor").toString());
        Message resp = new Message(Message.OP.COMPLETION_LIST).put("id", paragraphId);

        if (paragraphId == null) {
            conn.send(Serializer.getInstance().serializeMessage(resp));
        }else {
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            Paragraph p = note.getParagraph(paragraphId);
            List<String> candidates = p.completion(buffer, cursor);
            resp.put("completions", candidates);
            conn.send(Serializer.getInstance().serializeMessage(resp));
        }
    }
}
