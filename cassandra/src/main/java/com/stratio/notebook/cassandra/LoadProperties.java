package com.stratio.notebook.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by afidalgo on 11/09/15.
 */
public class LoadProperties {
    static Logger logger = LoggerFactory.getLogger(LoadProperties.class);

    public  static Properties load() {
        try {

            Properties prop = new Properties();
            InputStream input = LoadProperties.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(input);
            return prop;
        }catch(IOException e){
            String msg = "File properties not loaded. ";
            logger.error(msg);
            throw new RuntimeException(msg,e); //TODO why a runtime Exception?¿
        }


    }

}
