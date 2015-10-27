package com.stratio.explorer.reader;


import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.exceptions.FileConfNotExisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private static Logger Logger = LoggerFactory.getLogger(PropertiesReader.class);

    /**
     * Read configuration file from file
     * @param nameFile name file configuration without extension
     * @return Map with key values
     */
    public Properties readConfigFrom(String nameFile) {

       Properties prop = new Properties();
        String path = "";
        try {
            path = new PathFileCalculator().getPath(nameFile, ConstantsFolder.CT_EXTENSION_FILE_PROPERTIES);
            prop.load(new FileInputStream(path));
        }catch(IOException e){
            String msg = "File properties not loaded. ";
            Logger.error(msg);
            throw new FileConfNotExisException("File properties "+path +" NOT FOUND "+e.getMessage());
        }
        return prop;
    }
}
