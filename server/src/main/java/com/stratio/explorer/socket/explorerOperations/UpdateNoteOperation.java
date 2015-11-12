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
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import org.java_websocket.WebSocket;

import java.util.Map;

/**
 * Created by jmgomez on 3/09/15.
 */
public class UpdateNoteOperation implements IExplorerOperation {
    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        String noteId = (String) messagereceived.get("id");
        String name = (String) messagereceived.get("name");
        Map<String, Object> config = (Map<String, Object>) messagereceived.get("config");
        if (noteId != null && config!=null) {
            Note note = notebook.getNote(noteId);
            if (note != null) {
                boolean cronUpdated = isCronUpdated(config, note.getConfig());
                note.setName(name);
                note.setConfig(config);
                if (cronUpdated) {
                    notebook.refreshCron(note.id());
                }
                ConnectionManager.getInstance().broadcastNote(note);
                new BroadcastNoteListOperation().execute(conn, notebook, messagereceived);
            }
        }
    }

    private boolean isCronUpdated(Map<String, Object> configA,
                                  Map<String, Object> configB) {
        boolean cronUpdated = false;
        if (configA.get("cron") != null && configB.get("cron") != null
                && configA.get("cron").equals(configB.get("cron"))) {
            cronUpdated = true;
        } else if (configA.get("cron") == null && configB.get("cron") == null) {
            cronUpdated = false;
        } else if (configA.get("cron") != null || configB.get("cron") != null) {
            cronUpdated = true;
        }
        return cronUpdated;
    }
}
