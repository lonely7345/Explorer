package com.stratio.notebook.reader;


import com.stratio.notebook.exceptions.FolderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    static Logger Logger = LoggerFactory.getLogger(FolderFinder.class);

    /**
     * Read configuration file from file
     * @param nameFile name file configuration without extension
     * @return Map with key values
     */
    public Properties readConfigFrom(String nameFile) {

       Properties prop = new Properties();
        String path = "";
        try {
            path = new PathFileCalculator().getPath(nameFile);
            prop.load(new FileInputStream(path));
        }catch(IOException e){
            String msg = "File properties not loaded. ";
            Logger.error(msg);
            throw new FolderNotFoundException("File properties "+path +" NOT FOUND "+e.getMessage());
        }
        return prop;
    }
}
