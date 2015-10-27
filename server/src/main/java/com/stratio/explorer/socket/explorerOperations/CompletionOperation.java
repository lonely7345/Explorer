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
