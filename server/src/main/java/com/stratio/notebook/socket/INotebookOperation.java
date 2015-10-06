package com.stratio.notebook.socket;

import com.stratio.notebook.notebook.Notebook;
import org.java_websocket.WebSocket;

/**
 * Created by jmgomez on 3/09/15.
 */
public interface INotebookOperation {

    /**
     * This method execute the NoteBook Opereation.
     * @param conn the websocket.
     * @param notebook the notebook
     * @param messagereceived the message.
     *
     * @throw NotebookOperationException if a exception happens.
     *
     */
    void execute(WebSocket conn, Notebook notebook,  Message messagereceived) throws NotebookOperationException;
}
