package com.stratio.notebook.socket;

import com.google.gson.Gson;

/**
 * This class must serialzer the messages.
 * Created by jmgomez on 3/09/15.
 */
public class Serializer {

    /**
     * The singleston instance.
     */
    private static Serializer serializer;
    /**
     * The gson serializer.
     */
    private Gson gson = new Gson();

    private Serializer (){}

    /**
     * This method recovered a singleton instance of serializer.
     * @return the singleton instance of serializer.
     */
    public synchronized  static Serializer getInstance() {
        if (serializer == null) {
            serializer = new Serializer();
        }
        return serializer;
    }

    public String serializeMessage(Message m) {
        return gson.toJson(m);
    }
}

