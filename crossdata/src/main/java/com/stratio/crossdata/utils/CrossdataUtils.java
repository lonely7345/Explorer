/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.utils;

import java.util.Map;

import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ConnectResult;
import com.stratio.crossdata.common.result.ConnectToConnectorResult;
import com.stratio.crossdata.common.result.DisconnectResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.InProgressResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.common.result.StorageResult;

public class CrossdataUtils {

    public static String resultToString(Result result) {
        if (ErrorResult.class.isInstance(result)) {
            return ErrorResult.class.cast(result).getErrorMessage();
        }
        if (result instanceof QueryResult) {
            QueryResult queryResult = (QueryResult) result;
            return queryResultToString(queryResult);
        } else if (result instanceof CommandResult) {
            CommandResult commandResult = (CommandResult) result;
            return String.class.cast(commandResult.getResult());
        } else if (result instanceof ConnectResult) {
            ConnectResult connectResult = (ConnectResult) result;
            return String.valueOf(connectResult.getSessionId());
        } else if (result instanceof DisconnectResult){
            DisconnectResult disconnectResult = (DisconnectResult) result;
            return String.valueOf("Disconnected from SessionId=" + disconnectResult.getSessionId());
        } else if (result instanceof MetadataResult) {
            MetadataResult metadataResult = (MetadataResult) result;
            return metadataResult.toString();
        } else if (result instanceof StorageResult) {
            StorageResult storageResult = (StorageResult) result;
            return storageResult.toString();
        } else if (result instanceof InProgressResult) {
            InProgressResult inProgressResult = (InProgressResult) result;
            return "Query " + inProgressResult.getQueryId() + " in progress";
        } else if (result instanceof ConnectToConnectorResult) {
            ConnectToConnectorResult connectResult = (ConnectToConnectorResult) result;
            if (connectResult.hasError()){
                return String.valueOf("ERROR: Couldn't connect to cluster: "+connectResult.getException().getMessage());
            }else{
                return String.valueOf("Connected to cluster successfully");
            }
        } else {
            return "Unknown result";
        }
    }

    public static String queryResultToString(QueryResult result) {
        StringBuilder sb = new StringBuilder();
        if (result.getResultSet().isEmpty()) {
            return "%text EMPTY result";
        }

        ResultSet resultSet = null;
        resultSet = result.getResultSet();
        sb.append("%table ");

        for (ColumnMetadata c : resultSet.getColumnMetadata()) {

            sb.append(c.getName().getColumnNameToShow()).append("\t");
        }
        sb.replace(sb.length() - 1, sb.length(), "\n");

        for (Row r : resultSet.getRows()) {
            for (Map.Entry<String, Cell> c : r.getCells().entrySet()) {
                sb.append(c.getValue()).append("\t");
            }
            sb.replace(sb.length() - 1, sb.length(), "\n");
        }

        return sb.toString();
    }
}
