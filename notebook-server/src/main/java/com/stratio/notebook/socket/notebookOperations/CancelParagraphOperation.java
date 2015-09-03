package com.stratio.notebook.socket.notebookOperations;

import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public class CancelParagraphOperation implements com.stratio.notebook.socket.INotebookOperation {
    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        final String paragraphId = (String) messagereceived.get("id");
        if (paragraphId != null) {
           final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            Paragraph p = note.getParagraph(paragraphId);
            p.abort();
        }
    }
}
