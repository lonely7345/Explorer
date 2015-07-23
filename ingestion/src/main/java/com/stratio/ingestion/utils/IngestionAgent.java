package com.stratio.ingestion.utils;

/**
 * Created by idiaz on 15/07/15.
 */
public class IngestionAgent {
    private String name;
    private String filepath;
    private int port;

    public IngestionAgent(){
    }
    public IngestionAgent(String name, String filepath, int port){
        this.name=name;
        this.filepath=filepath;
        this.port=port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String toString() {
        return "IngestionAgent{" +
                "name='" + name + '\'' +
                ", filepath='" + filepath + '\'' +
                ", port=" + port +
                '}';
    }
}
