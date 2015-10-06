package com.stratio.notebook.socket.notebookOperations;

import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by jmgomez on 3/09/15.
 */
public class BroadcastNoteListOperation implements com.stratio.notebook.socket.INotebookOperation {

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
