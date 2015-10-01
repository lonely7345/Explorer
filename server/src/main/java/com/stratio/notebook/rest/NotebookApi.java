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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.config.service.AtmosphereService;
import org.atmosphere.jersey.JerseyBroadcaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.stratio.notebook.notebook.Notebook;

@Path("/notebook")
/*
@AtmosphereService(
        dispatch = false,
        interceptors = {AtmosphereResourceLifecycleInterceptor.class},
        path = "/notebook",
        servlet = "org.glassfish.jersey.servlet.ServletContainer")
        */
@Produces("application/json")
//@AtmosphereService(path="/")
@AtmosphereService (broadcaster = JerseyBroadcaster.class)
public class NotebookApi {
	Logger logger = LoggerFactory.getLogger(NotebookApi.class);

	private Notebook notebook;

	public NotebookApi(){
	}
	
	public NotebookApi(Notebook notebook) {
		this.notebook = notebook;
	}

	@Suspend	
	@GET
	public String suspend(){
		return "";
	}
	
	@Broadcast(writeEntity = false)
	@POST
	@Produces("application/json")
	public NotebookResponse broadcast(String message) {
		return new NotebookResponse("hello");
	}
}
