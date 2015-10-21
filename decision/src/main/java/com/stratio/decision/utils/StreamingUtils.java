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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stratio.decision.api.messaging.ColumnNameType;
import com.stratio.decision.api.messaging.ColumnNameValue;
import com.stratio.decision.commons.constants.ColumnType;
import com.stratio.decision.commons.exceptions.StratioStreamingException;
import com.stratio.decision.commons.messages.ColumnNameTypeValue;
import com.stratio.decision.commons.messages.StreamQuery;
import com.stratio.decision.commons.streams.StratioStream;

/**
 * Created by idiaz on 24/06/15.
 */
public class StreamingUtils {

    public static String listToString(List<StratioStream> streams) {
        StringBuilder sb = new StringBuilder();
        sb.append("Stream name\tUser defined\tQueries\tElements\tActive actions\n");
        for (StratioStream stream : streams) {
            sb.append(stream.getStreamName()).append("\t");
            sb.append(stream.getUserDefined()).append("\t");
            sb.append(stream.getQueries().size()).append("\t");
            sb.append(stream.getColumns().size()).append("\t");
            sb.append(stream.getActiveActions().toString()).append("\n");
            sb.append(renderQueriesTable(stream.getQueries(),stream.getStreamName()));
        }
        return sb.toString();
    }

    private static String renderQueriesTable(List<StreamQuery> queries, String stream) {
        StringBuilder sb = new StringBuilder();
        if (queries != null && queries.size() != 0) {
            sb.append("%table Id\tQuery raw\n");
            for (StreamQuery streamQuery : queries) {
                sb.append(streamQuery.getQueryId()).append("\t");
                sb.append(streamQuery.getQuery()).append("\n");
            }
        }
        return sb.toString();
    }

    public static String listQueriesToString(List<ColumnNameTypeValue> columns){
        StringBuilder sb = new StringBuilder();
        sb.append("column\ttype\n");
        for(ColumnNameTypeValue column: columns){
            sb.append(column.getColumn()).append("\t");
            sb.append(column.getType()).append("\n");
//            sb.append(column.getValue()).append("\n");
        }

        return sb.toString();
    }

    public static List<ColumnNameType> stringToColumnNameTypeList(String value) throws StratioStreamingException {

        List<ColumnNameType> result = new ArrayList<>();
        String[] keyValueArray = value.split(",");
        for (String keyValueString : keyValueArray) {
            String[] keyTypeArray = keyValueString.split("\\.");
            if (keyTypeArray.length != 2) {
                throw new StratioStreamingException("Error processing (".concat(keyValueString).concat(")"));
            }

            String key = keyTypeArray[0].trim();
            String type = keyTypeArray[1].trim();
            try {
                result.add(new ColumnNameType(key, ColumnType.valueOf(type.toUpperCase())));
            } catch (IllegalArgumentException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Type not found");
                sb.append(" (".concat(type).concat(") "));
                sb.append(" Available types: ".concat(Arrays.asList(ColumnType.values()).toString()));
                throw new StratioStreamingException(sb.toString());
            }
        }
        return result;
    }

    public static List<ColumnNameValue> stringToColumnNameValueList (String input) throws StratioStreamingException {
        String[] keyValueArray = input.split(",");
        List<ColumnNameValue> result = new ArrayList<>();
        for (String keyValueString : keyValueArray) {
            String[] nameValueArray = keyValueString.split("\\.");
            if (nameValueArray.length != 2) {
                throw new StratioStreamingException("Error processing (".concat(keyValueString).concat(")"));
            }

            String name = nameValueArray[0].trim();
            String value = nameValueArray[1].trim();
            result.add(new ColumnNameValue(name, value));
        }
        return result;
    }
}
