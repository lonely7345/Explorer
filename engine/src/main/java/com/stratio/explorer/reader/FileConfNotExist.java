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

import com.stratio.explorer.exceptions.FileConfNotExisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Called when not exist fileConf
 */
public class FileConfNotExist implements FileConfLocator{

    private static org.slf4j.Logger Logger = LoggerFactory.getLogger(FileConfNotExist.class);

    /**
     * Allwais thorw FileConfNotexistException
     * @param fileName name file
     * @param extensionFile extension file
     * @return
     */
    @Override
    public String locate(String fileName, String extensionFile) {
        String message = "File configuration "+fileName+"."+extensionFile+" not exist";
        Logger.error(message);
        throw new FileConfNotExisException(message);
    }
}
