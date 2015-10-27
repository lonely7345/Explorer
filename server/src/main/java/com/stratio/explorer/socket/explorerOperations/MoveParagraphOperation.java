package com.stratio.explorer.socket.explorerOperations;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import com.stratio.explorer.socket.ExplorerOperationException;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jmgomez on 3/09/15.
 */
public class MoveParagraphOperation implements IExplorerOperation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNoteOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws ExplorerOperationException {
        try{
        final String paragraphId = (String) messagereceived.get("id");
        if (paragraphId != null) {
            final int newIndex = (int) Double.parseDouble(messagereceived.get("index").toString());
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            note.moveParagraph(paragraphId, newIndex);
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
        }
    }catch(IOException ioe){
        String msg = "A exception happens in when we are trying to move a paragraph."+ioe.getMessage();
        LOG.error(msg);
        throw new ExplorerOperationException(msg,ioe);
    }
    }
}
