/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.rest;

import com.stratio.explorer.converters.PropertiesToStringConverter;
import com.stratio.explorer.exceptions.FileConfNotExisException;
import com.stratio.explorer.interpreter.InterpreterException;
import com.stratio.explorer.interpreter.InterpreterFactory;
import com.stratio.explorer.reader.PropertiesReader;
import com.stratio.explorer.server.JsonResponse;
import com.stratio.explorer.writer.PropertiesFileUpdater;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Interpreter Rest API
 */
@Path("/interpreter")
@Produces("application/json")
@Api(value = "/interpreter", description = "Explorer Interpreter REST API")
public class InterpreterRestApi {

    private InterpreterFactory interpreterFactory;

    public InterpreterRestApi(InterpreterFactory interpreterFactory) {
        this.interpreterFactory = interpreterFactory;
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
        Response response= new JsonResponse(Response.Status.OK, "", newProperties).build();
        try {
            PropertiesFileUpdater updater = new PropertiesFileUpdater();
            updater.updateFileWithProperties("ingestion", newProperties);
        } catch (FileConfNotExisException e) {
            response = new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        }
        return response;
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
        } catch (FileConfNotExisException e) {
            return new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", newProperties).build();
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
    @Path("settings/crossdata")
    public Response updateCrossdataSettings(String newProperties) {

        Response response= new JsonResponse(Response.Status.OK, "", newProperties).build();
        try {
            PropertiesFileUpdater updater = new PropertiesFileUpdater();
            updater.updateFileWithProperties("driver-application", newProperties);
        } catch (FileConfNotExisException e) {
            response = new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        }
        return response;
    }


    /**
     * List all properties from file with name file
     * @param nameFile nameFile without extension
     * @return Empty JsonResponse
     */
    @GET
    @Path("settings/list")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "When something goes wrong") })
    public Response listPropertiesFrom(@QueryParam("nameFile") String nameFile) {
        PropertiesToStringConverter converter = new PropertiesToStringConverter(System.lineSeparator());
        return new JsonResponse(Response.Status.OK, "", converter.transform(new PropertiesReader().readConfigFrom(nameFile))).build();
    }
}