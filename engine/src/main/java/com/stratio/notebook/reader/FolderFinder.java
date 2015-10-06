package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;
import com.stratio.notebook.exceptions.FolderNotFoundException;

import java.nio.file.Path;
import java.util.List;

public class FolderFinder {


    /**
     * Calculate relative path to rootFolder
     * @return string with rootParentFolder
     */
    public String parentProjectFolder() {

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
