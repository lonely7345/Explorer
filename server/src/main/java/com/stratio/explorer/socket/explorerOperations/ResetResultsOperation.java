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

/**
 * Created by jmgomez on 3/09/15.
 */
public class ResetResultsOperation implements IExplorerOperation {
    private static final Logger LOG = LoggerFactory.getLogger(ResetResultsOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws ExplorerOperationException {
       try {

            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            for (Paragraph p : note.getParagraphs()) {
                p.resetResult();
            }
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
        }catch(IOException ioe){
            String msg = "A exception happens in when we are trying to reset result."+ioe.getMessage();
            LOG.error(msg);
            throw  new ExplorerOperationException(msg,ioe);
        }
    }
}
