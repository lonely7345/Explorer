/*
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

package com.stratio.explorer.reader;

import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.functions.SearchFileFunction;
import com.stratio.explorer.functions.SearcherFunction;
import com.stratio.explorer.lists.FunctionalList;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by afidalgo on 26/10/15.
 */
public class FileConfLocator {

    private static Logger Logger = LoggerFactory.getLogger(FileConfLocator.class);
    private File folder;
    /**
     * Constructor init with
     */
    public FileConfLocator(){
        try {
            URL url = getClass().getClassLoader().getResource(".");
            folder = new File(url.toURI().getPath());
        }catch (URISyntaxException e){
            Logger.error("Class FileConfLocator have not been  loaded ");
        }
    }

    //TODO : CHANGE TO NOT VISITED FOLDER MORE ONE TIME
    /**
     * locate any file into tree folders
     * @param fileName file to search
     * @return String with path
     */
    public String locate(String fileName)  {
       FunctionalList<File, File> functional = new FunctionalList(new ArrayList<>(FileUtils.listFiles(folder, null, true)));
       List<File> files = functional.search(new SearchFileFunction(fileName));
       String result = pathIfexistFiles(files);
       if (noRootFolder() && result.isEmpty()) {
            folder = new File(folder.getParent());
            result = locate(fileName);
       }
       return result;
    }

    private String pathIfexistFiles(List<File> files){
       String result ="";
        if (!files.isEmpty()) {
            result = files.get(0).getPath();
        }
        return result;
    }

    private boolean noRootFolder(){
        return (!folder.getPath().endsWith(ConstantsFolder.CT_NAME_PROJECT_FOLDER) &&
                !folder.getPath().endsWith(ConstantsFolder.CT_PRODUCTION_FOLDER) );
    }
}
