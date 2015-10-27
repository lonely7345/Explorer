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

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IngestionAgent that = (IngestionAgent) o;

        if (port != that.port) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        return filepath.equals(that.filepath);

    }

    @Override public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + filepath.hashCode();
        result = 31 * result + port;
        return result;
    }
}
