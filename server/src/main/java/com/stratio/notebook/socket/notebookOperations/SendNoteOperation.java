package com.stratio.notebook.socket.notebookOperations;

import com.google.gson.Gson;
import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import com.stratio.notebook.socket.Serializer;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public class SendNoteOperation implements com.stratio.notebook.socket.INotebookOperation {



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
