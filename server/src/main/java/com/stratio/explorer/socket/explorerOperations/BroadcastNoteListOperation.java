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
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmgomez on 3/09/15.
 */
public class BroadcastNoteListOperation implements IExplorerOperation {

    //TODO review if we can not use parameter in the execute interface.
    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        List<Note> notes = notebook.getAllNotes();
        List<Map<String, String>> notesInfo = new LinkedList<Map<String, String>>();
        for (Note note : notes) {
            Map<String, String> info = new HashMap<String, String>();
            info.put("id", note.id());
            info.put("name", note.getName());
            info.put("date", note.getCreationDate());
            notesInfo.add(info);
        }
        ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.NOTES_INFO).put("notes", notesInfo));
    }



}
