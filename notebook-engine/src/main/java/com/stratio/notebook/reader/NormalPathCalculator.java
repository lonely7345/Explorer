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

package com.stratio.notebook.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NormalPathCalculator implements PathCalculator {

    static Logger LOGGER = LoggerFactory.getLogger(FolderFinder.class);

    private final String NOT_POSSIBLE_FOLDER ="¿?";
    private String stringFolder;

    public NormalPathCalculator(String stringFolder){
        this.stringFolder = stringFolder;
    }

    /**+
     * Calculate path from stringFolder
     * @return Path from Stringfolder
     */
    @Override
    public Path calculatePath(){
        Path path =null;
        try {
            URL url = getClass().getClassLoader().getResource(".");
            path = Paths.get(url.toURI());
            while(path!=null &&
                    !path.endsWith(stringFolder)){
                path = path.getParent();
            }

        }catch(URISyntaxException e){
            String msg ="Error with sintax in : "+e.getMessage();
            LOGGER.error(msg);
        }

        return generatePath(path);
    }

    private Path generatePath(Path path){
        if (path==null)
            path = Paths.get(NOT_POSSIBLE_FOLDER);
        return path;
    }
}
