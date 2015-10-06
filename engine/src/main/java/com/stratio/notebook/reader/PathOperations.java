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
package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathOperations {

    private Path path;

    public PathOperations(Path path){
        this.path = path;
    }


    public boolean noFinishFolder(){
        return !ConstantsFolder.CT_NOT_EXIST_FOLDER.equals(path.toString());
    }

    public boolean notFileExist(){
        return Files.notExists(path);
    }

    public  void appendFolderToPath(String folder){

        if (path==null) {
            path = Paths.get(ConstantsFolder.CT_NOT_EXIST_FOLDER);
        }else {
            path = Paths.get(path.toString(), folder);
        }
    }

    public void goParent(){
        path = path.getParent().getParent();
    }

    public Path getPath(){
        return path;
    }
}
