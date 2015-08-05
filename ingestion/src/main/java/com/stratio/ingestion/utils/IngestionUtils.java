package com.stratio.ingestion.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionUtils {
    static Logger logger = LoggerFactory.getLogger(IngestionUtils.class);

    public static String getAgentName(String filepath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line = br.readLine();
        while (line != null) {
            if (!line.trim().startsWith("#")) {
                String[] split = line.trim().split("\\.");
                return split[0];
            }
            line = br.readLine();
        }
        return null;
    }

    public static DefaultExecuteResultHandler executeBash(String command) throws IOException {
        CommandLine cmdLine = CommandLine.parse("bash");
        cmdLine.addArgument("-c", false);
        cmdLine.addArgument(command, false);

        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStreamAgentStart = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStreamAgentStart));
        DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
        executor.execute(cmdLine, handler);

        return handler;
    }

    public static void getIngestionPropertiesFiles(List<String> files, String path) {

        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                getIngestionPropertiesFiles(files, f.getAbsolutePath());
            } else {
                String filename = f.getName();
                String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

                if (extension.equals("properties")){
                    files.add(f.getAbsolutePath());
                }
            }
        }
    }

    public static Map<String, String> readConfig() {
        Map<String, String> settings = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getRelativeDir("conf/ingestion/ingestion.conf")));
            String line = br.readLine();
            while (line != null) {
                String[] lineArray = line.trim().split("=");
                settings.put(lineArray[0], lineArray[1]);
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return settings;
    }

    public static String getRelativeDir(String path) {
        if (System.getenv("NOTEBOOK_HOME")!=null) {
            return System.getenv("NOTEBOOK_HOME") + "/" + path;
        } else {
            String rootPath = IngestionUtils.class.getResource("").getPath();
            rootPath = rootPath.substring(0,rootPath.length()-"/ingestion/target/classes/com/stratio/ingestion/utils"
                    .length());
            return rootPath+ "/" + path;
        }
    }

    public static Map<String, String> getChannelsStatus(String json) {

        Map<String, String> channelsStatus = new HashMap<>();
        JSONObject agentStatusJson = new JSONObject(json);
        for (Object key : agentStatusJson.keySet()) {
            String stringKey = String.valueOf(key);
            if (stringKey.contains("CHANNEL")) {
                String[] channelKey = stringKey.split("\\.");
                String channelName = channelKey[1]; // name is after dot
                String channelValue = agentStatusJson.getJSONObject(stringKey).getString("ChannelFillPercentage");
                channelsStatus.put(channelName, channelValue);
            }
        }
        return channelsStatus;
    }

    public static String getAgentStatus(int port) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://localhost:".concat(String.valueOf(port)).concat("/metrics"));
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static String help() {
        return "List of ingestion interpreter commands\n"
                + "* agent start - start the selected agent on selected port\n"
                + "* agent stop - stop the selected agent on selected port\n"
                + "* channels status - gives channel's size status of active channels\n"
                + "* list properties - shows a list of filepaths with agent's properties\n"
                + "* edit - opens a simple editor to modify the selected .properties file";

    }
}
