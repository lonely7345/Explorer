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
import com.stratio.explorer.exceptions.FolderNotFoundException;

import java.nio.file.Path;
import java.util.List;


public class PathFileCalculator {


    /**
     * Obtain complete path to file
     * @param nameFile without extension
     * @return complete path
     */
    public String getPath(String nameFile,String extensionFile){


        return new FileConfLocator().locate(nameFile+extensionFile);

     //   return parentProjectFolder() +nameFile+extensionFile;
    }



    private String parentProjectFolder() {

        List<PathCalculator> pathCalculators = PathCalculatorListBuilder.build();
        for (PathCalculator pathCalculator:pathCalculators){
            Path path = pathCalculator.calculatePath();
            if (!path.toString().equals(ConstantsFolder.CT_NOT_EXIST_FOLDER)) {
                return path.toString() + "/";
            }

        }
        throw new FolderNotFoundException("Folder not exist ");
    }

}
