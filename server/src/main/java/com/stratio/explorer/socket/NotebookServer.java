/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stratio.explorer.socket;

import com.google.gson.Gson;
import com.stratio.explorer.notebook.JobListenerFactory;
import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.scheduler.Job;
import com.stratio.explorer.scheduler.JobListener;
import com.stratio.explorer.server.ZeppelinServer;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Zeppelin websocket service.
 *
 * @author anthonycorbacho
 */
public class NotebookServer extends WebSocketServer implements JobListenerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(NotebookServer.class);
    private static final int DEFAULT_PORT = 8282;

    private static void creatingwebSocketServerLog(int port) {
        LOG.info("Create explorer websocket on port {}", port);
    }

    Gson gson = new Gson();


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
        ConnectionManager.getInstance().add(conn);

    }

    @Override
    public void onMessage(WebSocket conn, String msg) {
         try {
            Message messagereceived = deserializeMessage(msg);

            LOG.info("RECEIVE << " + messagereceived.op);
            NotebookOperationFactory.getOperation(messagereceived.op).execute(conn,notebook(),messagereceived);
        } catch (NotebookOperationException e) {
             //TODO why we don't do anithing with this exception.
            LOG.error("Can't handle message.", e);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOG.info("Closed connection to {} : {}", conn.getRemoteSocketAddress().getHostName(),
                conn.getRemoteSocketAddress().getPort());
        ConnectionManager conectorManager = ConnectionManager.getInstance();
        conectorManager.removeConnectionFromAllNote(conn);
        conectorManager.remove(conn);

    }

    @Override
    public void onError(WebSocket conn, Exception message) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.removeConnectionFromAllNote(conn);
        connectionManager.remove(conn);

    }

    private Message deserializeMessage(String msg) {
        Message m = gson.fromJson(msg, Message.class);
        return m;
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
        final Note note = notebook.getNote(ConnectionManager.getInstance().getOpenNoteId(conn));
        Paragraph p = note.getParagraph(paragraphId);
        String query = (String) fromMessage.get("paragraph");

        //check datavis connection
        //post through datavis api endpoint -> careful about CORS header
        p.setReturn(postQueryDatavis(query));
        ConnectionManager.getInstance().broadcast(note.id(), new Message(Message.OP.PARAGRAPH).put("paragraph", p));

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
            ConnectionManager.getInstance().broadcast(note.id(),
                    new Message(Message.OP.PROGRESS).put("id", job.getId()).put("progress", job.progress()));

        }

        @Override
        public void beforeStatusChange(Job job, Job.Status before, Job.Status after) {
        }

        @Override
        public void afterStatusChange(Job job, Job.Status before, Job.Status after) {
            if (before != Job.Status.REFRESH_RESULT) {
                if (after == Job.Status.PENDING) {
                    //reset data on screen
                    job.setReturn("%text ");
                }
                if (after == Job.Status.ERROR) {
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
                    ConnectionManager.getInstance().broadcast(note.id(), new Message(Message.OP.PARAGRAPH).put("paragraph", p));
                } else {
                    ConnectionManager.getInstance().broadcastNote(note);
                }
            }
        }
    }

    @Override
    public JobListener getParagraphJobListener(Note note) {
        return new ParagraphJobListener(this, note);
    }
}
