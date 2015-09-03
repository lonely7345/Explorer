package com.stratio.notebook.socket.notebookOperations;

import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.socket.ConnectionManager;
import com.stratio.notebook.socket.Message;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jmgomez on 3/09/15.
 */
public class ExportNoteOperation implements com.stratio.notebook.socket.INotebookOperation {

    private static final Logger LOG = LoggerFactory.getLogger(ExportNoteOperation.class);


    @Override
    public void execute(WebSocket conn, Notebook notebook, Message messagereceived) {
        try {
            String filename = (String) messagereceived.get("filename");
            String path = "export";
            String id = (String) messagereceived.get("id");
            Note note = notebook.getNote(id);
            LOG.debug("export Note " + filename + " " + id);

            note.exportToFile(path, filename);
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.EXPORT_INFO).put("info", "Exported successfully in " + path));
        } catch (IOException e) {
            LOG.info("We are catch a IOException when we try to export a note but we continue."+e.getMessage());
            ConnectionManager.getInstance().broadcastAll(new Message(Message.OP.EXPORT_INFO).put("info", "Could not load the file"));
        }
    }
}
