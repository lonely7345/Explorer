package com.stratio.notebook.socket.notebookOperations;

import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.notebook.Paragraph;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jmgomez on 3/09/15.
 */
public class ImportNoteOperation implements com.stratio.notebook.socket.INotebookOperation {


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

            //TODO review this exception without log. Throwable catch all exceptions.
        } catch (Throwable e) {
            logger.info("We are catch a IOException when we try to import a note but we continue." + e.getMessage());
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.IMPORT_INFO).put("info", e.getMessage()));
        }

    }
}
