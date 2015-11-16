package com.stratio.explorer.reader;

import com.stratio.explorer.conf.ConstantsFolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Run all configured locators.
 */
public class LocatorsRunner implements FileConfLocator{



    private List<FileConfLocator> locators = new ArrayList<>();


    /**
     * Constructor with all locators
     */
    public LocatorsRunner(){
        locators.add(new FileConfByNameFileLocator());
        locators.add(new FileConfByEnviromentLocator(ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV));
    }

    /**
     * run all locators in order
     * @param nameFile
     * @param extension
     * @return first locator that return valid value
     */
    public String locate(String nameFile,String extension){
        String path ="";
        for (FileConfLocator locator:locators) {
            if ("".equals(path)){
                path =  locator.locate(nameFile,extension);
            }
        }
        return path;
    }
}