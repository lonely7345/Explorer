package com.stratio.explorer.reader;


import com.stratio.explorer.conf.ConstantsFolder;
import com.stratio.explorer.exceptions.FolderNotFoundException;

import java.nio.file.Path;
import java.util.List;


public class PathFileCalculator {


    /**
     * Obtain complete path to file
     * @param nameFile without extension
     * @return complete path
     */
    public String getPath(String nameFile,String extensionFile){


        return parentProjectFolder() +nameFile+extensionFile;
    }



    private String parentProjectFolder() {

        List<PathCalculator> pathCalculators = PathCalculatorListBuilder.build();
        for (PathCalculator pathCalculator:pathCalculators){
            Path path = pathCalculator.calculatePath();
            if (!path.toString().equals(ConstantsFolder.CT_NOT_EXIST_FOLDER)) {
                return path.toString() + "/";
            }

        }
        throw new FolderNotFoundException("Folder not exist ");
    }

}
