package com.stratio.explorer.reader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NormalPathCalculator implements PathCalculator {

    static Logger Logger = LoggerFactory.getLogger(NormalPathCalculator.class);

    private String configureFolder;

    public NormalPathCalculator(String configureFolder){
        this.configureFolder = configureFolder;
    }

    /**+
     * Calculate path from stringFolder
     * @return Path from configureFile
     */
    @Override
    public Path calculatePath(){
        PathOperations pathOperations = null;
        try {
             URL url = getClass().getClassLoader().getResource(".");
             pathOperations = new PathOperations(Paths.get(Paths.get(url.toURI()).toString(),configureFolder));
            while(pathOperations.noFinishFolder()&&pathOperations.notFileExist()){
               pathOperations.goParent();
               pathOperations.appendFolderToPath(configureFolder);
            }

        }catch(URISyntaxException e){
            String msg ="Error with sintax in : "+e.getMessage();
            Logger.error(msg);
        }

        return pathOperations.getPath();

    }
}
