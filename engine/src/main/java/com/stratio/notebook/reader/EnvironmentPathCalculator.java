package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//TODO : METHOD CODE WILL NEED CLEAN
public class EnvironmentPathCalculator implements PathCalculator{

    private String stringFolder;

    public EnvironmentPathCalculator(String stringFolder){
        this.stringFolder = stringFolder;
    }

    /**
     * Calculate path from enviroment variable
     * @return Path of stringfolder
     */
    @Override
    public Path calculatePath() {
        String env = System.getenv(stringFolder);
        if (env==null)
            return Paths.get(ConstantsFolder.CT_NOT_EXIST_FOLDER);
        Path path  = Paths.get(env);
        if (Files.notExists(path))
            return Paths.get(ConstantsFolder.CT_NOT_EXIST_FOLDER);
        return  path;
    }
}
