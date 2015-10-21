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
package com.stratio.explorer.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.server.ExplorerServer;
import com.stratio.explorer.socket.ConnectionManager;
import com.stratio.explorer.socket.ExplorerWebSocketServer;
import com.wordnik.swagger.annotations.Api;

/**
 * @author anthonycorbacho
 * @since 0.3.4
 */
@Path("/")
@Api(value = "/", description = "Zeppelin REST API root")
public class ExplorerRestApi {

    private ExplorerWebSocketServer notebookServer;

    /**
     * Required by Swagger
     */
    public ExplorerRestApi() {
        super();
    }

    public void setExplorerWebSocketServer(ExplorerWebSocketServer ns) {
        this.notebookServer = ns;
    }

    /**
     * Get the root endpoint
     * Return always 200
     *
     * @return 200 response
     */
    @GET
    public Response getRoot() {
        return Response.ok().build();
    }

    @Path("/add/query")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postQuery(@FormParam("notebook") String notebook, @FormParam("query") String query) {

        String notebookId = "";
        List<Note> notes = ExplorerServer.notebook.getAllNotes();
        for (Note n : notes) {
            if (notebook.equalsIgnoreCase(n.getName()) || notebook.equalsIgnoreCase(n.id())) {
                notebookId = n.id();
                break;
            }
        }
        try {
            final Note note = ExplorerServer.notebook
                    .getNote(notebookId);
            final int paragraphIndex = note.getParagraphs().size() - 1;
            Paragraph paragraph = note.insertParagraph(paragraphIndex);
            paragraph.setText(query);
            try {
                note.persist();
            } catch (IOException e) {
                return Response.status(500)
                        .entity("Could not persist the changes: " + e.getMessage())
                        .build();
            }

            ConnectionManager.getInstance().broadcastNote(note);
        } catch (Exception e) {
            return Response.status(400)
                    .entity("Notebook name or id not valid")
                    .build();
        }

        return Response.status(200)
                .entity("Added query: " + query + ", to notebook: " + notebookId)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postQueryNoParams() {
        return Response.status(400)
                .entity("Notebook and query form params required")
                .build();
    }

    @Path("/add/queries")
    @POST
    public Response postQueries() {
        return Response.ok().build();
    }

    @Path("/add/notebook")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postNotebook(@FormParam("name") String name) {

        Note note = ExplorerServer.notebook.createNote();
        if (!name.isEmpty()) {
            note.setName(name);
        }else{
            name = "Unnamed";
        }

        note.addParagraph(); // it's an empty note. so add one paragraph
        try {
            note.persist();
        } catch (IOException e) {
            return Response.status(500)
                    .entity("Could not persist the changes: " + e.getMessage())
                    .build();
        }
        ConnectionManager.getInstance().broadcastNote(note);
        ConnectionManager.getInstance().broadcastNoteList();



        return Response.status(200)
                .entity("Notebook: " + name + " with id: " + note.id() + " created")
                .build();
    }
}

