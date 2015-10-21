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
package com.stratio.decision.utils;

import java.util.List;



import com.stratio.decision.api.StratioStreamingAPI;
import com.stratio.decision.api.messaging.ColumnNameType;
import com.stratio.decision.api.messaging.ColumnNameValue;
import com.stratio.decision.commons.exceptions.StratioStreamingException;
import com.stratio.decision.commons.messages.ColumnNameTypeValue;
import com.stratio.decision.commons.streams.StratioStream;

/**
 * Created by idiaz on 24/06/15.
 */
public class StreamingApiWrapper {
    private StratioStreamingAPI api;

    public StreamingApiWrapper(StratioStreamingAPI api) {
        this.api = api;
    }

    /**
     * Action commands
     */

    public String indexStart(String stream) throws StratioStreamingException {
        api.indexStream(stream);
        return "%text Stream ".concat(stream).concat(" indexed correctly");
    }

    public String indexStop(String stream) throws StratioStreamingException {
        api.stopIndexStream(stream);
        return "%text Stream ".concat(stream).concat(" unindexed correctly");
    }

    public String saveCassandraStart(String stream) throws StratioStreamingException {
        api.saveToCassandra(stream);
        return "%text Stream ".concat(stream).concat(" attached to cassandra correctly");
    }

    public String saveCassandraStop(String stream) throws StratioStreamingException {
        api.stopSaveToCassandra(stream);
        return "%text Stream ".concat(stream).concat(" de-attached from cassandra correctly");
    }

    public String saveMongoStart(String stream) throws StratioStreamingException {
        api.saveToMongo(stream);
        return "%text Stream ".concat(stream).concat(" attached to mongo correctly");
    }

    public String saveMongoStop(String stream) throws StratioStreamingException {
        api.stopSaveToMongo(stream);
        return "%text Stream ".concat(stream).concat(" de-attached from mongo correctly");
    }

    public String saveSolrStart(String stream) throws StratioStreamingException {
        api.saveToSolr(stream);
        return "%text Stream ".concat(stream).concat(" attached to mongo correctly");
    }

    public String saveSolrStop(String stream) throws StratioStreamingException {
        api.stopSaveToSolr(stream);
        return "%text Stream ".concat(stream).concat(" de-attached from mongo correctly");
    }

    public String listenStart(String stream) throws StratioStreamingException {
        api.listenStream(stream);
        return "%text Stream ".concat(stream).concat(" listened correctly");
    }

    public String listenStop(String stream) throws StratioStreamingException {
        api.stopListenStream(stream);
        return "%text Stream ".concat(stream).concat("listener removed");
    }

    /**
     * Query commands
     */

    public String createQuery(String stream, String query) throws StratioStreamingException {
        String queryId = api.addQuery(stream, query);
        return "%text Query ".concat(stream).concat(" created correctly with id ".concat(queryId));
    }

    public String removeQuery(String stream, String id) throws StratioStreamingException {
        api.removeQuery(stream, id);
        return "%text Query ".concat(stream).concat(" dropped correctly with id ".concat(id));
    }

    /**
     * Stream commands
     */

    public String list() throws StratioStreamingException {
        List<StratioStream> list = api.listStreams();
        return "%table ".concat(StreamingUtils.listToString(list));

    }

    public String listQuerys(String stream) throws StratioStreamingException {
        List<ColumnNameTypeValue> columnsValues = api.columnsFromStream(stream);

        return "%table ".concat(StreamingUtils.listQueriesToString(columnsValues));
    }

    public String create(String stream, List<ColumnNameType> columns) throws StratioStreamingException {
        api.createStream(stream, columns);
        return "%text Stream ".concat(stream).concat(" created correctly");
    }

    public String drop(String stream) throws StratioStreamingException {
        api.dropStream(stream);
        return "%text Stream ".concat(stream).concat(" dropped correctly");

    }

    public String alter(String stream, List<ColumnNameType> columns) throws StratioStreamingException {
        api.alterStream(stream, columns);
        return "%text Stream ".concat(stream).concat(" altered correctly");

    }

    public String insert(String stream, List<ColumnNameValue> values) throws StratioStreamingException {
        api.insertData(stream, values);
        return "%text Added an event to stream ".concat(stream).concat(" correctly");

    }
}
