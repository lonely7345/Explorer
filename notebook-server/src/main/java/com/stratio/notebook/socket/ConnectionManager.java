package com.stratio.notebook.socket;

import com.stratio.notebook.notebook.Note;
import com.stratio.notebook.notebook.Notebook;
import com.stratio.notebook.server.ZeppelinServer;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by jmgomez on 3/09/15.
 */
public class ConnectionManager {

    //TODO maybe this class must be refactor in two or more classes.
    /**
     * The Log.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);


    /**
     * The singleton instance.
     */
    private static ConnectionManager connectionManager = null;
    /**
     * The map which store the notes with their connections.
     */
    private static Map<String, List<WebSocket>> noteSocketMap = new HashMap<String, List<WebSocket>>();


    /**
     * The connected socket.
     */
    private List<WebSocket> connectedSockets = new LinkedList<WebSocket>();

    /**
     * Constructor.
     */
    private ConnectionManager() {
    }

    /**
     * Recover a singleton instance.
     *
     * @return a singleton instance.
     */
    public static synchronized ConnectionManager getInstance() {
        if (connectionManager == null) {
            connectionManager = new ConnectionManager();
        }
        return connectionManager;
    }


    //TODO review synchronization to evaluate throughput improve.
    public void addConnectionToNote(String noteId, WebSocket socket) {
        synchronized (noteSocketMap) {
            removeConnectionFromAllNote(socket); // make sure a socket relates only a single note.
            List<WebSocket> socketList = noteSocketMap.get(noteId);
            if (socketList == null) {
                socketList = new LinkedList<WebSocket>();
                noteSocketMap.put(noteId, socketList);
            }

            if (socketList.contains(socket) == false) {
                socketList.add(socket);
            }
        }
    }

    public void removeConnectionFromAllNote(WebSocket socket) {
        synchronized (noteSocketMap) {
            Set<String> keys = noteSocketMap.keySet();
            for (String noteId : keys) {
                removeConnectionFromNote(noteId, socket);
            }
        }
    }

    public void removeConnectionFromNote(String noteId, WebSocket socket) {
        synchronized (noteSocketMap) {
            List<WebSocket> socketList = noteSocketMap.get(noteId);
            if (socketList != null) {
                socketList.remove(socket);
            }
        }
    }

    public void broadcastAll(Message m) {
        synchronized (connectedSockets) {
            for (WebSocket conn : connectedSockets) {
                conn.send(Serializer.getInstance().serializeMessage(m));
            }
        }
    }

    public void broadcastNote(Note note) {
        broadcast(note.id(), new Message(Message.OP.NOTE).put("note", note));
    }

    public void broadcast(String noteId, Message m) {
        LOG.info("SEND >> " + m.op);
        synchronized (noteSocketMap) {
            List<WebSocket> socketLists = noteSocketMap.get(noteId);
            //TODO review this conditions
            if (socketLists != null && socketLists.size() != 0) {
                for (WebSocket conn : socketLists) {
                    conn.send(Serializer.getInstance().serializeMessage(m));
                }
            }
        }
    }




    public void removeNote(String noteId) {
        synchronized (noteSocketMap) {
            noteSocketMap.remove(noteId);
        }
    }

    public String getOpenNoteId(WebSocket socket) {
        String id = null;
        synchronized (noteSocketMap) {
            Set<String> keys = noteSocketMap.keySet();
            for (String noteId : keys) {
                List<WebSocket> sockets = noteSocketMap.get(noteId);
                if (sockets.contains(socket)) {
                    id = noteId;
                }
            }
        }
        return id;
    }

    public void broadcastNoteList() {
        Notebook notebook = ZeppelinServer.notebook;
        List<Note> notes = notebook.getAllNotes();
        List<Map<String, String>> notesInfo = new LinkedList<Map<String, String>>();
        for (Note note : notes) {
            Map<String, String> info = new HashMap<String, String>();
            info.put("id", note.id());
            info.put("name", note.getName());
            info.put("date", note.getCreationDate());
            notesInfo.add(info);
        }
        broadcastAll(new Message(Message.OP.NOTES_INFO).put("notes", notesInfo));
    }
}



