/**
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
package com.stratio.notebook.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.stratio.notebook.converters.PropertiesToStringConverter;
import com.stratio.notebook.exceptions.FolderNotFoundException;
import com.stratio.notebook.reader.PropertiesReader;
import com.stratio.notebook.writer.PropertiesFileUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.stratio.notebook.interpreter.InterpreterException;
import com.stratio.notebook.interpreter.InterpreterFactory;
import com.stratio.notebook.server.JsonResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Interpreter Rest API
 */
@Path("/interpreter")
@Produces("application/json")
@Api(value = "/interpreter", description = "Notebook Interpreter REST API")
public class InterpreterRestApi {
    Logger logger = LoggerFactory.getLogger(InterpreterRestApi.class);

    private InterpreterFactory interpreterFactory;

    Gson gson = new Gson();

    public InterpreterRestApi() {

    }

    public InterpreterRestApi(InterpreterFactory interpreterFactory) {
        this.interpreterFactory = interpreterFactory;
    }

    public static class File {
        String path;
        String body;
    }

    /**
     * List all interpreter settings
     *
     * @return
     */
    @GET
    @Path("settings/crossdata")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "When something goes wrong") })
    public Response listCrossdataSettings() {
        String interpreterSettings = "";
        interpreterSettings = interpreterFactory.loadCrossdataSettings();
        return new JsonResponse(Response.Status.OK, "", interpreterSettings).build();
    }

    /**
     * Add new interpreter setting
     *
     * @param message
     * @return
     * @throws IOException
     * @throws InterpreterException
     */
    @PUT
    @Path("settings/crossdata")
    public Response updateCrossdataSettings(String message) {
        logger.info("Update interpreterSettings {}", message);

        try {
            interpreterFactory.saveCrossdataSettings(message);
        } catch (InterpreterException e) {
            return new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        } catch (IOException e) {
            return new JsonResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", message).build();
    }






    /**
     * List all interpreter settings
     *
     * @return
     */
    @GET
    @Path("settings/editor")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "When something goes wrong") })
    public Response getEditorSettings(@QueryParam("path") String path) {
        String interpreterSettings = "";
        interpreterSettings = interpreterFactory.loadEditorData(path);
        return new JsonResponse(Response.Status.OK, "", interpreterSettings).build();
    }

    /**
     * Set new value for editor file
     *
     * @return
     */
    @PUT
    @Path("settings/editor")
    public Response saveEditorSettings(String file) {
        String[] split = file.split("~");
        String path = split[0];
        String body = file.substring(path.length()+1);


        try {
            interpreterFactory.saveEditorSettings(body, path);
        } catch (IOException e) {
            return new JsonResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", file).build();
    }


    /**
     * Reset settings to default
     *
     * @param message
     * @return
     * @throws IOException
     * @throws InterpreterException
     */
    @PUT
    @Path("reset")
    public Response resetSettings(String message) {
        logger.info("Update interpreterSettings {}", message);

        try {
            interpreterFactory.loadCrossdataDefaultSettings();
        } catch (InterpreterException e) {
            return new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        } catch (IOException e) {
            return new JsonResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", message).build();
    }


    /**
     * Add new interpreter setting
     *
     * @param newProperties
     * @return
     * @throws IOException
     * @throws InterpreterException
     */
    @PUT
    @Path("settings/ingestion")
    public Response updateIngestionSettings(String newProperties) {
        try {
            PropertiesFileUpdater updater = new PropertiesFileUpdater();
            updater.updateFileWithProperties("cassandra", newProperties);
        } catch (FolderNotFoundException e) {
            return new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", newProperties).build();
    }


    /**
     * List all interpreter settings
     *
     * @return
     */
    @GET
    @Path("settings/ingestion")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "When something goes wrong") })
    public Response listIngestionSettings() {
        PropertiesToStringConverter converter = new PropertiesToStringConverter(System.lineSeparator());
        return new JsonResponse(Response.Status.OK, "", converter.transform(new PropertiesReader().readConfigFrom("ingestion"))).build();
    }

    /**
     * List all interpreter settings
     *
     * @return
     */
    @GET
    @Path("settings/cassandra")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "When something goes wrong") })
    public Response listCassandraSettings() {
        PropertiesToStringConverter converter = new PropertiesToStringConverter(System.lineSeparator());
        return new JsonResponse(Response.Status.OK, "", converter.transform(new PropertiesReader().readConfigFrom("cassandra"))).build();
    }

    /**
     * Add new interpreter setting
     *
     * @param newProperties
     * @return
     * @throws IOException
     * @throws InterpreterException
     */
    @PUT
    @Path("settings/cassandra")
    public Response updateCassandraSettings(String newProperties) {
        try {
            PropertiesFileUpdater updater = new PropertiesFileUpdater();
            updater.updateFileWithProperties("cassandra", newProperties);
        } catch (FolderNotFoundException e) {
            return new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", newProperties).build();
    }
}
