package com.stratio.streaming.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stratio.streaming.commons.exceptions.StratioStreamingException;

/**
 * Created by idiaz on 25/06/15.
 */
public class StreamingSyntaxParser {
    StreamingApiWrapper api;

    public StreamingSyntaxParser(StreamingApiWrapper api) {
        this.api = api;
    }

    enum Command {
        ADD_QUERY,
        ALTER,
        COLUMNS,
        CREATE,
        DROP,
        INDEX_START,
        INDEX_STOP,
        INSERT,
        LIST,
        LISTEN_START,
        LISTEN_STOP,
        REMOVE_QUERY,
        SAVE_CASSANDRA_START,
        SAVE_CASSANDRA_STOP,
        SAVE_MONGO_START,
        SAVE_MONGO_STOP,
        SAVE_SOLR_START,
        SAVE_SOLR_STOP,
        HELP
    }
     /**
      * This method must parse the streaming sentence.
      * @param input the streaming sentence.
      * @output the sentence parsed.
      *
      * @throws StratioStreamingException if any error happens.
      **/
    public String parse(String input) throws StratioStreamingException {
         //TODO review this method
        Map<Command, Pattern> commandPatterns = new HashMap<>();
        commandPatterns.put(Command.ADD_QUERY, Pattern.compile("(add query)(.*)")); //add query
        commandPatterns.put(Command.ALTER, Pattern.compile("(alter)(.*)")); //alter
        commandPatterns.put(Command.COLUMNS, Pattern.compile("(columns)(.*)")); //columns
        commandPatterns.put(Command.CREATE, Pattern.compile("(create)(.*)")); //create
        commandPatterns.put(Command.DROP, Pattern.compile("(drop)(.*)")); //drop
        commandPatterns.put(Command.INDEX_START, Pattern.compile("(index start)(.*)")); //index start
        commandPatterns.put(Command.INDEX_STOP, Pattern.compile("(index stop)(.*)")); //index stop
        commandPatterns.put(Command.INSERT, Pattern.compile("(^insert)(.*)")); //insert
        commandPatterns.put(Command.LIST, Pattern.compile("(list)")); //list
        commandPatterns.put(Command.LISTEN_START, Pattern.compile("(listen start)(.*)")); //listen start
        commandPatterns.put(Command.LISTEN_STOP, Pattern.compile("(listen stop)(.*)")); //listen stop
        commandPatterns.put(Command.REMOVE_QUERY, Pattern.compile("(remove query)(.*)")); //remove query
        commandPatterns.put(Command.SAVE_CASSANDRA_START,
                Pattern.compile("(save cassandra start)(.*)")); //save cassandra start
        commandPatterns
                .put(Command.SAVE_CASSANDRA_STOP, Pattern.compile("(save cassandra stop)(.*)")); //save cassandra stop
        commandPatterns.put(Command.SAVE_MONGO_START, Pattern.compile("(save mongo start)(.*)")); //save mongo start
        commandPatterns.put(Command.SAVE_MONGO_STOP, Pattern.compile("(save mongo stop)(.*)")); //save mongo stop
        commandPatterns.put(Command.SAVE_SOLR_START, Pattern.compile("(save solr start)(.*)")); //save solr start
        commandPatterns.put(Command.SAVE_SOLR_STOP, Pattern.compile("(save solr stop)(.*)")); //save solr stop
        commandPatterns.put(Command.HELP, Pattern.compile("(help)(.*)")); //help
        String inputNormalized = input.toLowerCase().trim(); // normalize
        for (Map.Entry<Command, Pattern> p : commandPatterns.entrySet()) {
            Matcher matcher = p.getValue().matcher(inputNormalized);
            if (matcher.find()) {
                String filtered = "";
                 if (matcher.groupCount()>1){
                     filtered = matcher.group(2);
                   }
                switch (p.getKey()) {
                case ADD_QUERY:
                    return addQuery(filtered);
                case ALTER:
                    return alterStream(filtered);
                case COLUMNS:
                    return columns(filtered);
                case CREATE:
                    return create(filtered);
                case DROP:
                    return drop(filtered);
                case INDEX_START:
                    return indexStart(filtered);
                case INDEX_STOP:
                    return indexStop(filtered);
                case INSERT:
                    return insert(filtered);
                case LIST:
                    return list(filtered);
                case LISTEN_START:
                    return listenStart(filtered);
                case LISTEN_STOP:
                    return listenStop(filtered);
                case REMOVE_QUERY:
                    return removeQuery(filtered);
                case SAVE_CASSANDRA_START:
                    return saveCassandraStart(filtered);
                case SAVE_CASSANDRA_STOP:
                    return saveCassandraStop(filtered);
                case SAVE_MONGO_START:
                    return saveMongoStart(filtered);
                case SAVE_MONGO_STOP:
                    return saveMongoStop(filtered);
                case SAVE_SOLR_START:
                    return saveSolrStart(filtered);
                case SAVE_SOLR_STOP:
                    return saveSolrStop(filtered);
                case HELP:
                    return help();
                default:
                    return "%text Command not supported";
                }
            }

        }
        return "%text Invalid Streaming command";
    }

    private String addQuery(String input) throws StratioStreamingException {

        Pattern addQuery = Pattern.compile("( --stream) ([\\w]+) (--definition) ([\\w *'=()#\\.]+)");
        Matcher addQueryMatch = addQuery.matcher(input);
        if (addQueryMatch.find()) {
            return api.createQuery(addQueryMatch.group(2), addQueryMatch.group(4));
        }
        return "%text invalid add query syntax. Use the following pattern as guide: add query --stream your_stream_name "
                + "--definition query";
    }

    private String columns(String input) throws StratioStreamingException {

        Pattern columns = Pattern.compile("( --stream) ([\\w]+)");
        Matcher columnsMatch = columns.matcher(input);
        if (columnsMatch.find()) {
            return api.listQuerys(columnsMatch.group(2));
        }
        return "%text invalid columns syntax. Use the following pattern as guide: columns --stream your_stream_name ";
    }

    private String drop(String input) throws StratioStreamingException {

        Pattern drop = Pattern.compile("( --stream) ([\\w]+)");
        Matcher dropMatch = drop.matcher(input);
        if (dropMatch.find()) {
            return api.drop(dropMatch.group(2));
        }
        return "%text invalid drop syntax. Use the following pattern as guide: drop --stream your_stream_name ";
    }

    private String indexStart(String input) throws StratioStreamingException {

        Pattern indexStart = Pattern.compile("( --stream) ([\\w]+)");
        Matcher indexStartMatch = indexStart.matcher(input);
        if (indexStartMatch.find()) {
            return api.indexStart(indexStartMatch.group(2));
        }
        return "%text invalid index start syntax. Use the following pattern as guide: index start --stream your_stream_name ";
    }

    private String indexStop(String input) throws StratioStreamingException {

        Pattern indexStop = Pattern.compile("( --stream) ([\\w]+)");
        Matcher indexStopMatch = indexStop.matcher(input);
        if (indexStopMatch.find()) {
            return api.indexStop(indexStopMatch.group(2));
        }
        return "%text invalid index stop syntax. Use the following pattern as guide: index stop --stream your_stream_name ";
    }

    private String list(String input) throws StratioStreamingException {
        return api.list();
    }

    private String listenStart(String input) throws StratioStreamingException {

        Pattern listenStart = Pattern.compile("( --stream) ([\\w]+)");
        Matcher listenStartMatch = listenStart.matcher(input);
        if (listenStartMatch.find()) {
            return api.listenStart(listenStartMatch.group(2));
        }
        return "%text invalid listen start syntax. Use the following pattern as guide: listen start --stream your_stream_name ";
    }

    private String listenStop(String input) throws StratioStreamingException {

        Pattern listenStop = Pattern.compile("( --stream) ([\\w]+)");
        Matcher listenStopMatch = listenStop.matcher(input);
        if (listenStopMatch.find()) {
            return api.listenStop(listenStopMatch.group(2));
        }
        return "%text invalid listen stop syntax. Use the following pattern as guide: listen stop --stream your_stream_name ";
    }

    private String removeQuery(String input) throws StratioStreamingException {

        Pattern removeQuery = Pattern.compile("( --stream) ([\\w]+) (--id) ([\\w *'=()]+)");
        Matcher removeQueryMatch = removeQuery.matcher(input);
        if (removeQueryMatch.find()) {
            return api.removeQuery(removeQueryMatch.group(2), removeQueryMatch.group(4));
        }
        return "%text invalid add query syntax. Use the following pattern as guide: add query --stream your_stream_name "
                + "--id query";
    }

    private String saveCassandraStart(String input) throws StratioStreamingException {

        Pattern saveCassandraStart = Pattern.compile("( --stream) ([\\w]+)");
        Matcher saveCassandraStartMatch = saveCassandraStart.matcher(input);
        if (saveCassandraStartMatch.find()) {
            return api.saveCassandraStart(saveCassandraStartMatch.group(2));
        }
        return "%text invalid save cassandra start syntax. Use the following pattern as guide: save cassandra start "
                + "--stream your_stream_name ";
    }

    private String saveCassandraStop(String input) throws StratioStreamingException {

        Pattern saveCassandraStop = Pattern.compile("( --stream) ([\\w]+)");
        Matcher saveCassandraStopMatch = saveCassandraStop.matcher(input);
        if (saveCassandraStopMatch.find()) {
            return api.saveCassandraStop(saveCassandraStopMatch.group(2));
        }
        return "%text invalid save cassandra stop syntax. Use the following pattern as guide: save cassandra stop "
                + "--stream your_stream_name ";
    }

    private String saveMongoStart(String input) throws StratioStreamingException {

        Pattern saveMongoStart = Pattern.compile("( --stream) ([\\w]+)");
        Matcher saveMongoStartMatch = saveMongoStart.matcher(input);
        if (saveMongoStartMatch.find()) {
            return api.saveMongoStart(saveMongoStartMatch.group(2));
        }
        return "%text invalid save mongo start syntax. Use the following pattern as guide: save mongo start "
                + "--stream your_stream_name ";
    }

    private String saveMongoStop(String input) throws StratioStreamingException {

        Pattern saveMongoStop = Pattern.compile("( --stream) ([\\w]+)");
        Matcher saveMongoStopMatch = saveMongoStop.matcher(input);
        if (saveMongoStopMatch.find()) {
            return api.saveMongoStop(saveMongoStopMatch.group(2));
        }
        return "%text invalid save mongo stop syntax. Use the following pattern as guide: save mongo stop "
                + "--stream your_stream_name ";
    }

    private String saveSolrStart(String input) throws StratioStreamingException {

        Pattern saveSolrStart = Pattern.compile("( --stream) ([\\w]+)");
        Matcher saveSolrStartMatch = saveSolrStart.matcher(input);
        if (saveSolrStartMatch.find()) {
            return api.saveSolrStart(saveSolrStartMatch.group(2));
        }
        return "%text invalid save mongo start syntax. Use the following pattern as guide: save mongo start "
                + "--stream your_stream_name ";
    }

    private String saveSolrStop(String input) throws StratioStreamingException {

        Pattern saveSolrStop = Pattern.compile("( --stream) ([\\w]+)");
        Matcher saveSolrStopMatch = saveSolrStop.matcher(input);
        if (saveSolrStopMatch.find()) {
            return api.saveSolrStop(saveSolrStopMatch.group(2));
        }
        return "%text invalid save mongo stop syntax. Use the following pattern as guide: save mongo stop "
                + "--stream your_stream_name ";
    }

    private String create(String input) throws StratioStreamingException {

        Pattern create = Pattern.compile("( --stream) ([\\w]+) (--definition) ([\\w *'=().,]+)");
        Matcher createMatch = create.matcher(input);
        if (createMatch.find()) {
            return api.create(createMatch.group(2), StreamingUtils.stringToColumnNameTypeList(createMatch.group(4)));
        }
        return "%text invalid create syntax. Use the following pattern as guide: create --stream your_stream_name "
                + "--definition column_name.column_type,column_name2.column_type2,column_name3.column_type3";
    }

    private String alterStream(String input) throws StratioStreamingException {

        Pattern alterStream = Pattern.compile("( --stream) ([\\w]+) (--definition) ([\\w *'=().,]+)");
        Matcher alterStreamMatch = alterStream.matcher(input);
        if (alterStreamMatch.find()) {
            return api.alter(alterStreamMatch.group(2), StreamingUtils.stringToColumnNameTypeList(alterStreamMatch
                    .group(4)));
        }
        return "%text invalid alter stream syntax. Use the following pattern as guide: create --stream your_stream_name "
                + "--definition column_name.column_type,column_name2.column_type2,column_name3.column_type3";
    }

    private String insert(String input) throws StratioStreamingException {

        Pattern insert = Pattern.compile("( --stream) ([\\w]+) (--definition) ([\\w *'=().,]+)");
        Matcher insertMatch = insert.matcher(input);
        if (insertMatch.find()) {
            return api.insert(insertMatch.group(2), StreamingUtils.stringToColumnNameValueList(insertMatch
                    .group(4)));
        }
        return "%text invalid insert syntax. Use the following pattern as guide: insert --stream your_stream_name "
                + "--definition column_name.column_value,column_name2.column_value2,column_name3.column_value3";
    }

    private String help(){
        return "%text * add query - create new query\n"
                + "* alter - alter existing stream\n"
                + "* columns - list all streams querys into engine\n"
                + "* create - create new stream\n"
                + "* drop - drop existing stream\n"
                + "* help - List all commands usage\n"
                + "* index start - index stream events\n"
                + "* index stop - stop index stream events\n"
                + "* insert - insert events into existing stream\n"
                + "* list - list all streams into engine\n"
                + "* listen start - attach stream to kafka topic\n"
                + "* listen stop - de-attach stream to kafka topic\n"
                + "* remove query - remove an existing query\n"
                + "* save cassandra start - start save to cassandra action\n"
                + "* save cassandra stop - stop save to cassandra action\n"
                + "* save mongo start - start save to mongo action\n"
                + "* save mongo stop - stop save to mongo action\n"
                + "* save solr start - start save to solr action\n"
                + "* save solr stop - stop save to solr action\n";
    }

}
