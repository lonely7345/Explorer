package com.stratio.notebook.converters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;


public class StringToPropertiesConverter {

    Logger Logger = LoggerFactory.getLogger(StringToPropertiesConverter.class);
    /**
     * Convert String in Properties Object
     * @param propertiesString
     * @return properties Object
     */
    public Properties transform(String propertiesString) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(propertiesString));
        }catch (IOException e){
            Logger.error("Error to load properties from String ");
        }
        return properties;
    }
}
