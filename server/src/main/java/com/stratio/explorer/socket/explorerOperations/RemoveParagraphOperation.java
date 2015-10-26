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
public class RemoveParagraphOperation implements IExplorerOperation {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveParagraphOperation.class);


    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws ExplorerOperationException {
    try{
        final String paragraphId = (String) messagereceived.get("id");
        if (paragraphId != null) {
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            /** We dont want to remove the last paragraph */
            if (!note.isLastParagraph(paragraphId)) {
                note.removeParagraph(paragraphId);
                note.persist();
                ConnectionManager.getInstance().broadcastNote(note);
            }
        }
    }catch(IOException ioe){
        String msg = "A exception happens in when we are trying to remove a paragraph."+ioe.getMessage();
        LOG.error(msg);
        throw  new ExplorerOperationException(msg,ioe);
        }
    }
}
