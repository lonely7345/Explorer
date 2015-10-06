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
package com.stratio.notebook.spark.dep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.AbstractRepositoryListener;
import org.sonatype.aether.RepositoryEvent;


public class RepositoryListener extends AbstractRepositoryListener {
	Logger logger = LoggerFactory.getLogger(RepositoryListener.class);
	
	public RepositoryListener() {
	}

	public void artifactDeployed(RepositoryEvent event) {
		logger.info("Deployed " + event.getArtifact() + " to " + event.getRepository());
	}

	public void artifactDeploying(RepositoryEvent event) {
		logger.info("Deploying " + event.getArtifact() + " to " + event.getRepository());
	}

	public void artifactDescriptorInvalid(RepositoryEvent event) {
		logger.info("Invalid artifact descriptor for " + event.getArtifact() + ": " + event.getException().getMessage());
	}

	public void artifactDescriptorMissing(RepositoryEvent event) {
		logger.info("Missing artifact descriptor for " + event.getArtifact());
	}

	public void artifactInstalled(RepositoryEvent event) {
		logger.info("Installed " + event.getArtifact() + " to "	+ event.getFile());
	}

	public void artifactInstalling(RepositoryEvent event) {
		logger.info("Installing " + event.getArtifact() + " to " + event.getFile());
	}

	public void artifactResolved(RepositoryEvent event) {
		logger.info("Resolved artifact " + event.getArtifact() + " from "+ event.getRepository());
	}

	public void artifactDownloading(RepositoryEvent event) {
		logger.info("Downloading artifact " + event.getArtifact() + " from " + event.getRepository());
	}

	public void artifactDownloaded(RepositoryEvent event) {
		logger.info("Downloaded artifact " + event.getArtifact() + " from "	+ event.getRepository());
	}

	public void artifactResolving(RepositoryEvent event) {
		logger.info("Resolving artifact " + event.getArtifact());
	}

	public void metadataDeployed(RepositoryEvent event) {
		logger.info("Deployed " + event.getMetadata() + " to " + event.getRepository());
	}

	public void metadataDeploying(RepositoryEvent event) {
		logger.info("Deploying " + event.getMetadata() + " to "	+ event.getRepository());
	}

	public void metadataInstalled(RepositoryEvent event) {
		logger.info("Installed " + event.getMetadata() + " to "	+ event.getFile());
	}

	public void metadataInstalling(RepositoryEvent event) {
		logger.info("Installing " + event.getMetadata() + " to " + event.getFile());
	}

	public void metadataInvalid(RepositoryEvent event) {
		logger.info("Invalid metadata " + event.getMetadata());
	}

	public void metadataResolved(RepositoryEvent event) {
		logger.info("Resolved metadata " + event.getMetadata() + " from " + event.getRepository());
	}

	public void metadataResolving(RepositoryEvent event) {
		logger.info("Resolving metadata " + event.getMetadata() + " from " + event.getRepository());
	}
}
