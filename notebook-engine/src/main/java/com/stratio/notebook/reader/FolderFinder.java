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


import com.stratio.notebook.conf.ConstantsFolder;
import com.stratio.notebook.exceptions.FolderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderFinder {

    static Logger Logger = LoggerFactory.getLogger(FolderFinder.class);
    /**
     * Calculate relative path to rootFolder
     * @return string with rootParentFolder
     */
    public String parentProjectFolder() {
        return buildRootPathString(calculateRootFolder());
    }


    private Path calculateRootFolder(){
        Path path =null;
        try {
            URL url = getClass().getClassLoader().getResource(".");
            path = Paths.get(url.toURI());
            while(path!=null &&
                  !path.endsWith(ConstantsFolder.CT_NAME_PROJECT_FOLDER)){
                path = path.getParent();
            }

        }catch(URISyntaxException e){
            String msg ="Error with sintax in : "+e.getMessage();
            Logger.error(msg);
        }

        return path;
    }

    private String buildRootPathString(Path path){
        if (path==null)
           throw new FolderNotFoundException("Folder "+ConstantsFolder.CT_NAME_PROJECT_FOLDER +" not exist ");
        return path.toString() + "/";
    }
}
