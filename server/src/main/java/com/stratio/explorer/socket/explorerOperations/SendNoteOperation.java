package com.stratio.explorer.socket.explorerOperations;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import com.stratio.explorer.socket.Serializer;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public class SendNoteOperation implements IExplorerOperation {



    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        String noteId = (String) messagereceived.get("id");
        if (noteId != null) {
            Note note = notebook.getNote(noteId);
            if (note != null) {
                ConnectionManager.getInstance().addConnectionToNote(note.id(), conn);
                conn.send(Serializer.getInstance().serializeMessage(new Message(Message.OP.NOTE).put("note", note)));
            }
        }
    }





}
