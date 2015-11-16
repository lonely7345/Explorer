/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.reader;

import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.functions.SearchFileFunction;
import com.stratio.explorer.lists.FunctionalList;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Locate path by name file
 */
public class FileConfByNameFileLocator implements FileConfLocator{

    private static Logger Logger = LoggerFactory.getLogger(FileConfByNameFileLocator.class);
    private File folder;
    /**
     * Constructor init with folder of class.
     */
    public FileConfByNameFileLocator(){
        try {
            URL url = getClass().getClassLoader().getResource(".");
            folder = new File(url.toURI().getPath());
        }catch (URISyntaxException e){
            Logger.error("Class FileConfLocator have not been  loaded ");
        }
    }

    //TODO : CHANGE TO NOT VISITED FOLDER MORE ONE TIME
    /**
     * locate any file into tree folders.
     * @param fileName file to search
     * @return String with path
     */
    public String locate(String fileName,String extensionFile)  {
        String result ="";
        try {
            String[] extensions = new String[]{extensionFile};
            FunctionalList<File, File> functional = new FunctionalList(new ArrayList<>(FileUtils.listFiles(folder, extensions, true)));
            List<File> files = functional.search(new SearchFileFunction(fileName + "." + extensionFile));
            result = pathIfexistFiles(files);
            if (noRootFolder() && result.isEmpty()) {
                folder = new File(folder.getParent());
                result = locate(fileName, extensionFile);
            }
        }catch (OutOfMemoryError e){
            Logger.info("Too many files");
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
