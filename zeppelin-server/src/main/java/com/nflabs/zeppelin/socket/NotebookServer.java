package com.nflabs.zeppelin.socket;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.nflabs.zeppelin.notebook.JobListenerFactory;
import com.nflabs.zeppelin.notebook.Note;
import com.nflabs.zeppelin.notebook.Notebook;
import com.nflabs.zeppelin.notebook.Paragraph;
import com.nflabs.zeppelin.scheduler.Job;
import com.nflabs.zeppelin.scheduler.Job.Status;
import com.nflabs.zeppelin.scheduler.JobListener;
import com.nflabs.zeppelin.server.ZeppelinServer;
import com.nflabs.zeppelin.socket.Message.OP;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Zeppelin websocket service.
 *
 * @author anthonycorbacho
 */
public class NotebookServer extends WebSocketServer implements JobListenerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(NotebookServer.class);
    private static final int DEFAULT_PORT = 8282;

    private static void creatingwebSocketServerLog(int port) {
        LOG.info("Create zeppeling websocket on port {}", port);
    }

    Gson gson = new Gson();
    Map<String, List<WebSocket>> noteSocketMap = new HashMap<String, List<WebSocket>>();
    List<WebSocket> connectedSockets = new LinkedList<WebSocket>();

    public NotebookServer() {
        super(new InetSocketAddress(DEFAULT_PORT));
        creatingwebSocketServerLog(DEFAULT_PORT);
    }

    public NotebookServer(int port) {
        super(new InetSocketAddress(port));
        creatingwebSocketServerLog(port);
    }

    private Notebook notebook() {
        return ZeppelinServer.notebook;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        LOG.info("New connection from {} : {}", conn.getRemoteSocketAddress().getHostName(), conn
                .getRemoteSocketAddress().getPort());
        synchronized (connectedSockets) {
            connectedSockets.add(conn);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String msg) {
        Notebook notebook = notebook();
        try {
            Message messagereceived = deserializeMessage(msg);
            LOG.info("RECEIVE << " + messagereceived.op);
            /** Lets be elegant here */
            switch (messagereceived.op) {
            case LIST_NOTES:
                broadcastNoteList();
                break;
            case GET_NOTE:
                sendNote(conn, notebook, messagereceived);
                break;
            case IMPORT_NOTE:
                importNote(notebook, messagereceived);
                break;
            case EXPORT_NOTE:
                exportNote(notebook, messagereceived);
                break;
            case NEW_NOTE:
                createNote(conn, notebook);
                break;
            case DEL_NOTE:
                removeNote(conn, notebook, messagereceived);
                break;
            case COMMIT_PARAGRAPH:
                updateParagraph(conn, notebook, messagereceived);
                break;
            case RUN_PARAGRAPH:
                runParagraph(conn, notebook, messagereceived);
                break;
            case CANCEL_PARAGRAPH:
                cancelParagraph(conn, notebook, messagereceived);
                break;
            case MOVE_PARAGRAPH:
                moveParagraph(conn, notebook, messagereceived);
                break;
            case SPLIT_INTO_PARAGRAPHS:
                splitIntoParagraphs(conn, notebook, messagereceived);
                break;
            case INSERT_PARAGRAPH:
                insertParagraph(conn, notebook, messagereceived);
                break;
            case PARAGRAPH_REMOVE:
                removeParagraph(conn, notebook, messagereceived);
                break;
            case NOTE_UPDATE:
                updateNote(conn, notebook, messagereceived);
                break;
            case SEND_TO_DATAVIS:
                sendToDatavis(conn, notebook, messagereceived);
                break;
            case COMPLETION:
                completion(conn, notebook, messagereceived);
                break;
            default:
                broadcastNoteList();
                break;
            }
        } catch (Exception e) {
            LOG.error("Can't handle message", e);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOG.info("Closed connection to {} : {}", conn.getRemoteSocketAddress().getHostName(),
                conn.getRemoteSocketAddress().getPort());
        removeConnectionFromAllNote(conn);
        synchronized (connectedSockets) {
            connectedSockets.remove(conn);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception message) {
        removeConnectionFromAllNote(conn);
        synchronized (connectedSockets) {
            connectedSockets.remove(conn);
        }
    }

    private Message deserializeMessage(String msg) {
        Message m = gson.fromJson(msg, Message.class);
        return m;
    }

    private String serializeMessage(Message m) {
        return gson.toJson(m);
    }

    private void addConnectionToNote(String noteId, WebSocket socket) {
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

    private void removeConnectionFromNote(String noteId, WebSocket socket) {
        synchronized (noteSocketMap) {
            List<WebSocket> socketList = noteSocketMap.get(noteId);
            if (socketList != null) {
                socketList.remove(socket);
            }
        }
    }

    private void removeNote(String noteId) {
        synchronized (noteSocketMap) {
            List<WebSocket> socketList = noteSocketMap.remove(noteId);
        }
    }

    private void removeConnectionFromAllNote(WebSocket socket) {
        synchronized (noteSocketMap) {
            Set<String> keys = noteSocketMap.keySet();
            for (String noteId : keys) {
                removeConnectionFromNote(noteId, socket);
            }
        }
    }

    private String getOpenNoteId(WebSocket socket) {
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

    private void importNote(Notebook notebook, Message fromMessage)  {
        String path = (String) fromMessage.get("path");
        String[] filePath = path.split("/");
        String filename = filePath[filePath.length-1];
        System.out.println("##### filename " + filename);
        Note note = null;
        try {
            note = notebook.importFromFile(filename, path);
            System.out.println("##### Noteid " + note.id());
            for(Paragraph p : note.getParagraphs()){
                System.out.println("##### Paragraph: "+p.getText());
            }
            note.persist();
            broadcastNote(note);
            broadcastNoteList();
            broadcastAll(new Message(OP.IMPORT_INFO).put("info","imported successfully"));
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            broadcastAll(new Message(OP.IMPORT_INFO).put("info", e.getMessage() ));
        }
    }

    private void exportNote(Notebook notebook, Message fromMessage)  {
        String filename = (String) fromMessage.get("filename");
        String path = "export";
        String id = (String) fromMessage.get("id");
        Note note = notebook.getNote(id);
        System.out.println("export Note "+ filename + " "+ id );
        try {
            note.exportToFile(path, filename);
            broadcastAll(new Message(OP.EXPORT_INFO).put("info", "Exported successfully in "+path ));
        } catch (IOException e) {
            broadcastAll(new Message(OP.EXPORT_INFO).put("info", e.getMessage() ));
        }
    }

    private void broadcast(String noteId, Message m) {
        LOG.info("SEND >> " + m.op);
        synchronized (noteSocketMap) {
            List<WebSocket> socketLists = noteSocketMap.get(noteId);
            if (socketLists == null || socketLists.size() == 0) {
                return;
            }
            for (WebSocket conn : socketLists) {
                conn.send(serializeMessage(m));
            }
        }
    }

    private void broadcastAll(Message m) {
        synchronized (connectedSockets) {
            for (WebSocket conn : connectedSockets) {
                conn.send(serializeMessage(m));
            }
        }
    }

    public void broadcastNote(Note note) {
        broadcast(note.id(), new Message(OP.NOTE).put("note", note));
    }

    public void broadcastNoteList() {
        Notebook notebook = notebook();
        List<Note> notes = notebook.getAllNotes();
        List<Map<String, String>> notesInfo = new LinkedList<Map<String, String>>();
        for (Note note : notes) {
            Map<String, String> info = new HashMap<String, String>();
            info.put("id", note.id());
            info.put("name", note.getName());
            info.put("date", note.getCreationDate());
            notesInfo.add(info);
        }
        broadcastAll(new Message(OP.NOTES_INFO).put("notes", notesInfo));
    }

    private void sendNote(WebSocket conn, Notebook notebook, Message fromMessage) {
        String noteId = (String) fromMessage.get("id");
        if (noteId == null) {
            return;
        }
        Note note = notebook.getNote(noteId);
        if (note != null) {
            addConnectionToNote(note.id(), conn);
            conn.send(serializeMessage(new Message(OP.NOTE).put("note", note)));
        }
    }

    private void updateNote(WebSocket conn, Notebook notebook, Message fromMessage) throws SchedulerException {
        String noteId = (String) fromMessage.get("id");
        String name = (String) fromMessage.get("name");
        Map<String, Object> config = (Map<String, Object>) fromMessage.get("config");
        if (noteId == null) {
            return;
        }
        if (config == null) {
            return;
        }
        Note note = notebook.getNote(noteId);
        if (note != null) {
            boolean cronUpdated = isCronUpdated(config, note.getConfig());
            note.setName(name);
            note.setConfig(config);

            if (cronUpdated) {
                notebook.refreshCron(note.id());
            }

            broadcastNote(note);
            broadcastNoteList();
        }
    }

    private boolean isCronUpdated(Map<String, Object> configA,
            Map<String, Object> configB) {
        boolean cronUpdated = false;
        if (configA.get("cron") != null && configB.get("cron") != null
                && configA.get("cron").equals(configB.get("cron"))) {
            cronUpdated = true;
        } else if (configA.get("cron") == null && configB.get("cron") == null) {
            cronUpdated = false;
        } else if (configA.get("cron") != null || configB.get("cron") != null) {
            cronUpdated = true;
        }
        return cronUpdated;
    }

    private void createNote(WebSocket conn, Notebook notebook) throws IOException {
        Note note = notebook.createNote();
        note.addParagraph(); // it's an empty note. so add one paragraph
        note.persist();
        broadcastNote(note);
        broadcastNoteList();
    }

    private void removeNote(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        String noteId = (String) fromMessage.get("id");
        if (noteId == null) {
            return;
        }
        Note note = notebook.getNote(noteId);
        note.unpersist();
        notebook.removeNote(noteId);
        removeNote(noteId);
        broadcastNoteList();
    }

    private void updateParagraph(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        String paragraphId = (String) fromMessage.get("id");
        if (paragraphId == null) {
            return;
        }
        Map<String, Object> params = (Map<String, Object>) fromMessage.get("params");
        Map<String, Object> config = (Map<String, Object>) fromMessage.get("config");
        final Note note = notebook.getNote(getOpenNoteId(conn));
        Paragraph p = note.getParagraph(paragraphId);
        p.settings.setParams(params);
        p.setConfig(config);
        p.setTitle((String) fromMessage.get("title"));
        p.setText((String) fromMessage.get("paragraph"));
        note.persist();
        broadcast(note.id(), new Message(OP.PARAGRAPH).put("paragraph", p));
    }

    private void removeParagraph(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final String paragraphId = (String) fromMessage.get("id");
        if (paragraphId == null) {
            return;
        }
        final Note note = notebook.getNote(getOpenNoteId(conn));
        /** We dont want to remove the last paragraph */
        if (!note.isLastParagraph(paragraphId)) {
            note.removeParagraph(paragraphId);
            note.persist();
            broadcastNote(note);
        }
    }

    private void completion(WebSocket conn, Notebook notebook, Message fromMessage) {
        String paragraphId = (String) fromMessage.get("id");
        String buffer = (String) fromMessage.get("buf");
        int cursor = (int) Double.parseDouble(fromMessage.get("cursor").toString());
        Message resp = new Message(OP.COMPLETION_LIST).put("id", paragraphId);

        if (paragraphId == null) {
            conn.send(serializeMessage(resp));
            return;
        }

        final Note note = notebook.getNote(getOpenNoteId(conn));
        Paragraph p = note.getParagraph(paragraphId);
        List<String> candidates = p.completion(buffer, cursor);
        resp.put("completions", candidates);
        conn.send(serializeMessage(resp));
    }

    private void moveParagraph(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final String paragraphId = (String) fromMessage.get("id");
        if (paragraphId == null) {
            return;
        }

        final int newIndex = (int) Double.parseDouble(fromMessage.get("index").toString());
        final Note note = notebook.getNote(getOpenNoteId(conn));
        note.moveParagraph(paragraphId, newIndex);
        note.persist();
        broadcastNote(note);
    }

    private void insertParagraph(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final int index = (int) Double.parseDouble(fromMessage.get("index").toString());

        final Note note = notebook.getNote(getOpenNoteId(conn));
        note.insertParagraph(index);
        note.persist();
        broadcastNote(note);

    }

    private void splitIntoParagraphs(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final int index = (int) Double.parseDouble(fromMessage.get("index").toString());
        final String script = fromMessage.get("paragraph").toString();
        final Note note = notebook.getNote(getOpenNoteId(conn));

        String[] paragraphs = script.split(";");

        note.removeParagraph(String.class.cast(fromMessage.get("id")));
        note.persist();
        broadcastNote(note);

        for (int i = 0; i < paragraphs.length; i++) {
            note.insertParagraph(index + i - 1,
                    paragraphs[i].replaceAll("(\\r|\\n)", "").concat(";"));
            note.persist();
            broadcastNote(note);

        }

    }

    private void cancelParagraph(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final String paragraphId = (String) fromMessage.get("id");
        if (paragraphId == null) {
            return;
        }

        final Note note = notebook.getNote(getOpenNoteId(conn));
        Paragraph p = note.getParagraph(paragraphId);
        p.abort();
    }

    private void runParagraph(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final String paragraphId = (String) fromMessage.get("id");
        if (paragraphId == null) {
            return;
        }
        final Note note = notebook.getNote(getOpenNoteId(conn));
        Paragraph p = note.getParagraph(paragraphId);
        String text = (String) fromMessage.get("paragraph");
        p.setText(text);
        p.setTitle((String) fromMessage.get("title"));
        Map<String, Object> params = (Map<String, Object>) fromMessage.get("params");
        p.settings.setParams(params);
        Map<String, Object> config = (Map<String, Object>) fromMessage.get("config");
        p.setConfig(config);

        // if it's the last paragraph, let's add a new one
        boolean isTheLastParagraph = note.getLastParagraph().getId().equals(p.getId());
        if (!Strings.isNullOrEmpty(text) && isTheLastParagraph) {
            note.addParagraph();
        }
        note.persist();
        broadcastNote(note);

        note.run(paragraphId);
    }

    private String postQueryDatavis(String query) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://localhost:9000/dataviews");
        String responseString = "";
        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("name", query));
        params.add(new BasicNameValuePair("query", query));
        params.add(new BasicNameValuePair("iDataSource", "1"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        //Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                responseString = IOUtils.toString(instream);
            } finally {
                instream.close();
            }
        }

        return responseString;
    }

    private void sendToDatavis(WebSocket conn, Notebook notebook, Message fromMessage) throws IOException {
        final String paragraphId = (String) fromMessage.get("id");
        if (paragraphId == null) {
            return;
        }
        final Note note = notebook.getNote(getOpenNoteId(conn));
        Paragraph p = note.getParagraph(paragraphId);
        String query = (String) fromMessage.get("paragraph");

        //check datavis connection
        //post through datavis api endpoint -> careful about CORS header
        p.setReturn(postQueryDatavis(query));
        broadcast(note.id(), new Message(OP.PARAGRAPH).put("paragraph", p));

    }

    public static class ParagraphJobListener implements JobListener {
        private NotebookServer notebookServer;
        private Note note;

        public ParagraphJobListener(NotebookServer notebookServer, Note note) {
            this.notebookServer = notebookServer;
            this.note = note;
        }

        @Override
        public void onProgressUpdate(Job job, int progress) {
            notebookServer.broadcast(note.id(),
                    new Message(OP.PROGRESS).put("id", job.getId()).put("progress", job.progress()));

        }

        @Override
        public void beforeStatusChange(Job job, Status before, Status after) {
        }

        @Override
        public void afterStatusChange(Job job, Status before, Status after) {
            if (before != Status.REFRESH_RESULT) {
                if (after == Status.PENDING) {
                    //reset data on screen
                    job.setReturn("%text ");
                }
                if (after == Status.ERROR) {
                    job.getException().printStackTrace();
                }
                if (job.isTerminated()) {
                    LOG.info("Job {} is finished", job.getId());
                    try {
                        note.persist();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (Paragraph.class.isInstance(job)) {
                    Paragraph p = Paragraph.class.cast(job);
                    notebookServer.broadcast(note.id(), new Message(OP.PARAGRAPH).put("paragraph", p));
                } else {
                    notebookServer.broadcastNote(note);
                }
            }
        }
    }

    @Override
    public JobListener getParagraphJobListener(Note note) {
        return new ParagraphJobListener(this, note);
    }
}
