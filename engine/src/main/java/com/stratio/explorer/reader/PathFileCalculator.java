package com.stratio.explorer.reader;


import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.exceptions.FileConfNotExisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PathFileCalculator {

    private static org.slf4j.Logger Logger = LoggerFactory.getLogger(PathFileCalculator.class);
    /**
     * Obtain complete path to file.
     * @param nameFile without extension
     * @return complete path
     */
    public String getPath(String nameFile,String extensionFile){

         String path = new FileConfLocator().locate(nameFile+extensionFile);
         if ("".equals(path)) {
             path = System.getenv(ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV);
         }
         if (path==null){
             String message = "File configuration "+nameFile+" not exist";
             Logger.info(message);
             throw new FileConfNotExisException(message);
         }

         return path;
    }
}
