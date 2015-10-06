package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;


public class PathFileCalculator {



    private FolderFinder rootFolderFinder = new FolderFinder();

    /**
     * Obtain complete path to file
     * @param nameFile without extension
     * @return complete path
     */
    public String getPath(String nameFile){


        return rootFolderFinder.parentProjectFolder() +nameFile+ConstantsFolder.CT_EXTENSION_FILE_PROPERTIES;
    }

}
