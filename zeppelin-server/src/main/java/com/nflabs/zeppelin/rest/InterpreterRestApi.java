package com.nflabs.zeppelin.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
 *
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

    /**
     * List all interpreter settings
     * @return
     */
    @GET
    @Path("settings")
    @ApiOperation(httpMethod = "GET", value = "List all interpreter setting")
    @ApiResponses(value = {@ApiResponse(code = 500, message = "When something goes wrong")})
    public Response listSettings() {
        String interpreterSettings = "";
        interpreterSettings = interpreterFactory.loadCrossdataSettings();
        return new JsonResponse(Response.Status.OK, "", interpreterSettings).build();
    }

    /**
     * Add new interpreter setting
     * @param message
     * @return
     * @throws IOException
     * @throws InterpreterException
     */
    @PUT
    @Path("settings")
    public Response updateSettings(String message) {
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
     * Reset settings to default
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
