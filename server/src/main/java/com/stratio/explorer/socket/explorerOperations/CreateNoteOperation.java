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
public class CreateNoteOperation implements IExplorerOperation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNoteOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws ExplorerOperationException
    {
        try {
            Note note = notebook.createNote();
            note.addParagraph(); // it's an empty note. so add one paragraph
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
            new BroadcastNoteListOperation().execute(conn, notebook, messagereceived);
        }catch(IOException ioe){
            String msg = "A exception happens in when we are trying to create a note."+ioe.getMessage();
            LOG.error(msg);
            throw new ExplorerOperationException(msg,ioe);
        }
    }
}
