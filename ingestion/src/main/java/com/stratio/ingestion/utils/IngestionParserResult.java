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

package com.stratio.ingestion.utils;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionParserResult {
    private IngestionSyntaxParser.Command command;
    private IngestionAgent agent;

    public IngestionParserResult(IngestionSyntaxParser.Command command) {
        this.command = command;
        this.agent=null;
    }

    public IngestionParserResult(IngestionSyntaxParser.Command command, IngestionAgent agent) {
        this.command = command;
        this.agent = agent;
    }

    public IngestionSyntaxParser.Command getCommand() {
        return command;
    }

    public void setCommand(IngestionSyntaxParser.Command command) {
        this.command = command;
    }

    public IngestionAgent getAgent() {
        return agent;
    }

    public void setAgent(IngestionAgent agent) {
        this.agent = agent;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngestionParserResult that = (IngestionParserResult) o;

        if (command != that.command) {
            return false;
        }
        return !(agent != null ? !agent.equals(that.agent) : that.agent != null);

    }

    @Override public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        return result;
    }
}
