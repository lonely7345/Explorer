package com.nflabs.zeppelin.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nflabs.zeppelin.interpreter.InterpreterException;
import com.nflabs.zeppelin.interpreter.InterpreterFactory;
import com.nflabs.zeppelin.server.JsonResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Interpreter Rest API
 */
@Path("/interpreter")
@Produces("application/json")
@Api(value = "/interpreter", description = "Zeppelin Interpreter REST API")
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
    @Path("settings/ingestion")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "When something goes wrong") })
    public Response listIngestionSettings() {
        String interpreterSettings = "";
        interpreterSettings = interpreterFactory.loadIngestionSettings();
        return new JsonResponse(Response.Status.OK, "", interpreterSettings).build();
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
     * Add new interpreter setting
     *
     * @param message
     * @return
     * @throws IOException
     * @throws InterpreterException
     */
    @PUT
    @Path("settings/ingestion")
    public Response updateIngestionSettings(String message) {
        logger.info("Update interpreterSettings {}", message);

        try {
            interpreterFactory.saveIngestionSettings(message);
        } catch (InterpreterException e) {
            return new JsonResponse(Response.Status.NOT_FOUND, e.getMessage(), e).build();
        } catch (IOException e) {
            return new JsonResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), e).build();
        }
        return new JsonResponse(Response.Status.OK, "", message).build();
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

}
