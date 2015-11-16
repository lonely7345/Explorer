package com.stratio.explorer.reader;

/**
 * Locate path file
 */
public interface FileConfLocator {

    /**
     * Obtaindf path of file
     * @param fileName name file
     * @param extensionFile extension file
     * @return absolute path of file
     */
    String locate(String fileName,String extensionFile);
}
