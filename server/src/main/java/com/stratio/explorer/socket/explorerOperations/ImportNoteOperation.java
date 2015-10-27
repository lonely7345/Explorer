package com.stratio.explorer.socket.explorerOperations;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.IExplorerOperation;
import com.stratio.explorer.socket.Message;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jmgomez on 3/09/15.
 */
public class ImportNoteOperation implements IExplorerOperation {


    private static final Logger logger = LoggerFactory.getLogger(ExportNoteOperation.class);

    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        try {
        String path = (String) messagereceived.get("path");
        String[] filePath = path.split("/");
        String filename = filePath[filePath.length-1];
            logger.debug("##### filename " + filename);
            Note note = notebook.importFromFile(filename, path);
            logger.debug("##### Noteid " + note.id());
            if (logger.isDebugEnabled()) {
                for (Paragraph p : note.getParagraphs()) {
                    logger.debug("##### Paragraph: " + p.getText());
                }
            }
            note.persist();
            ConnectionManager.getInstance().broadcastNote(note);
            new BroadcastNoteListOperation().execute(conn, notebook, messagereceived);
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.IMPORT_INFO).put("info", "Imported successfully"));
        } catch (IOException e) {
            logger.info("We are catch a IOException when we try to import a note but we continue." + e.getMessage());
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.IMPORT_INFO).put("info", e.getMessage()));
        }

    }
}
