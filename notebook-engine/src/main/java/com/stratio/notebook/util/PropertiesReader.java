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

package com.stratio.notebook.util;


import com.stratio.notebook.conf.ConstantsFolder;
import com.stratio.notebook.exceptions.FolderNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    static Logger Logger = LoggerFactory.getLogger(FolderFinder.class);

    private FolderFinder rootFolderFinder = new FolderFinder();

    /**
     * Read configuration file from path
     * @param path with configuration file
     * @return Map with key values
     */
    public Properties readConfigFrom(String path) {

        Properties prop = new Properties();
        try {
            path = rootFolderFinder.parentProjectFolder()+ ConstantsFolder.CT_FOLDER_CONFIGURATION+path;
            prop.load(new FileInputStream(path));
        }catch(IOException e){
            String msg = "File properties not loaded. ";
            Logger.error(msg);
        }catch (FolderNotFoundException e){
            Logger.error(e.getMessage());
        }
        return prop;
    }
}
