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
public class UpdateParagraphOperation implements IExplorerOperation {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParagraphOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) throws ExplorerOperationException {
        try {
            String paragraphId = (String) messagereceived.get("id");
            if (paragraphId == null) {
                return;
            }
            Map<String, Object> params = (Map<String, Object>) messagereceived.get("params");
            Map<String, Object> config = (Map<String, Object>) messagereceived.get("config");
            final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
            Paragraph p = note.getParagraph(paragraphId);
            p.settings.setParams(params);
            p.setConfig(config);
            p.setTitle((String) messagereceived.get("title"));
            p.setText((String) messagereceived.get("paragraph"));
            note.persist();
            ConnectionManager.getInstance().broadcast(note.id(), new Message(Message.OP.PARAGRAPH).put("paragraph", p));
        }catch(IOException ioe){
            String msg = "A exception happens in when we are trying to update paragraph a note"+ioe.getMessage();
            LOG.error(msg);
            throw new ExplorerOperationException(msg,ioe);
        }
    }
}
