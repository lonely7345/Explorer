package com.stratio.explorer.socket.explorerOperations;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import com.stratio.explorer.socket.ExplorerOperationException;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jmgomez on 3/09/15.
 */
public class SaveNoteOperation implements IExplorerOperation {

    private static final Logger LOG = LoggerFactory.getLogger(SaveNoteOperation.class);


    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws ExplorerOperationException {
        try {
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            Map<String, String> paragraphsText = (Map<String, String>) messagereceived.get("paragraphsText");
            for (String key : paragraphsText.keySet()) {
                Paragraph p = note.getParagraph(key);
                p.setText(paragraphsText.get(key));
            }
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
        }catch(IOException ioe){
            String msg = "A exception happens in when we are trying to save a note."+ioe.getMessage();
            LOG.error(msg);
            throw  new ExplorerOperationException(msg,ioe);
        }
    }
}
