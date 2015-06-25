package com.stratio.streaming;

import java.util.List;

import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;

/**
 * Created by idiaz on 24/06/15.
 */
public class StreamingUtils {

    public static String listToString(List<StratioStream> streams) {
        StringBuilder sb = new StringBuilder();
        sb.append("%table Stream name\tUser defined\tQueries\tElements\tActive actions\n");
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
        sb.append("%table column\ttype\tvalue");
        for(ColumnNameTypeValue column: columns){
            sb.append(column.getColumn()).append("\t");
            sb.append(column.getType()).append("\t");
            sb.append(column.getValue()).append("\n");
        }

        return sb.toString();
    }
}
