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

import java.util.ArrayList;
import java.util.List;

/**
 * Run all configured locators.
 */
public class LocatorsRunner implements FileConfLocator{



    private List<FileConfLocator> locators = new ArrayList<>();


    /**
     * Constructor with all locators
     */
    public LocatorsRunner(){
        locators.add(new FileConfByNameFileLocator());
        locators.add(new FileConfByEnviromentLocator(ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV));
    }

    /**
     * run all locators in order
     * @param nameFile
     * @param extension
     * @return first locator that return valid value
     */
    public String locate(String nameFile,String extension){
        String path ="";
        for (FileConfLocator locator:locators) {
            if ("".equals(path)){
                path =  locator.locate(nameFile,extension);
            }
        }
        return path;
    }
}
