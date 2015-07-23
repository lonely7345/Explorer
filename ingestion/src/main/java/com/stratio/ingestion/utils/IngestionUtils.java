package com.stratio.ingestion.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionUtils {

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

    public static int getPid(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line = br.readLine();
        return Integer.parseInt(line);
    }

    public static String getAgentProcessName(IngestionAgent agent) {
        return agent.getName().concat("_").concat(String.valueOf(agent.getPort()));
    }

    public static String executeBash(String command, int timeout) throws IOException {
        CommandLine cmdLine = CommandLine.parse("bash");
        cmdLine.addArgument("-c", false);
        cmdLine.addArgument(command, false);
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream outputStreamAgentStart = new ByteArrayOutputStream();
        executor.setStreamHandler(new PumpStreamHandler(outputStreamAgentStart));

        executor.setWatchdog(new ExecuteWatchdog(timeout));
        executor.execute(cmdLine);
        System.out.println(outputStreamAgentStart.toString());
        return outputStreamAgentStart.toString();
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
                if (f.getName().contains(".properties") && !f.getName().contains("log4j") && !f.getName().contains(
                        ".template")) {
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
        if (path != null && path.startsWith("/")) {
            return path;
        } else {
            return System.getenv("NOTEBOOK_HOME") + "/" + path;
        }
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
