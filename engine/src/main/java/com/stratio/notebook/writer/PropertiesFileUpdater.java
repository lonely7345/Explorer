/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package com.stratio.notebook.writer;


import com.stratio.notebook.converters.StringToPropertiesConverter;
import com.stratio.notebook.reader.FolderFinder;
import com.stratio.notebook.reader.PathFileCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileUpdater {

    private  StringToPropertiesConverter converter = new StringToPropertiesConverter();
    static   Logger Logger = LoggerFactory.getLogger(FolderFinder.class);
    /**
     * Update configuration File with properties
     * @param nameFile name of file without extension
     * @param stringWithProperties
     */
    public void updateFileWithProperties(String nameFile, String stringWithProperties) {
        try {
            Properties properties = converter.transform(stringWithProperties);
            FileOutputStream out = new FileOutputStream(new PathFileCalculator().getPath(nameFile));
            properties.store(out, "");
            out.close();
        }catch (FileNotFoundException e){
            Logger.error("File " + nameFile + " not found " + e.getMessage());
        }catch (IOException e){
            Logger.error("Problem write file properties  "+e.getMessage());
        }
    }
}
