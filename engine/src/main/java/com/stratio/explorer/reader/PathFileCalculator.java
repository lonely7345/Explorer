/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
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
import com.stratio.explorer.exceptions.FileConfNotExisException;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculate path
 */
public class PathFileCalculator {

    private List<FileConfLocator> locators = new ArrayList<>();


    /**
     * Constructor with all posible locators
     */
    public PathFileCalculator(){
        locators.add(new FileConfByNameFileLocator());
        locators.add(new FileConfByEnviromentLocator(ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV));
        locators.add(new FileConfNotExist());
    }
    /**
     * Obtain complete path to file.
     * @param nameFile without extension
     * @return complete path
     */
    public String getPath(String nameFile,String extensionFile){

        String path ="";
        for (int index=0;index<locators.size() && path.isEmpty();index++){
            FileConfLocator locator = locators.get(index);
            path =  locator.locate(nameFile,extensionFile);
        }
        return path;

    }
}
