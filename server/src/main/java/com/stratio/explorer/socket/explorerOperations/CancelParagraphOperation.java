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
import com.stratio.explorer.scheduler.Job;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public class CancelParagraphOperation implements IExplorerOperation {
    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        final String paragraphId = idParagraph( messagereceived);
        if (paragraphId != null && !paragraphId.isEmpty() )  {
           final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
           Paragraph paragraph = note.getParagraph(paragraphId);
           paragraph.setStatus(Job.Status.ABORT);
           paragraph.setListener(null);
        }
    }


    private String idParagraph(Message messagereceived){
        String paragraphId = (String) messagereceived.get("id");
        if (paragraphId == null) {
            paragraphId = "";
        }
        return paragraphId;
    }
}
