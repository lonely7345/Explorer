/**
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

