package com.stratio.explorer.reader;

/**
 * Return path of file configuration by envioroment Paths
 */
public class FileConfByEnviromentLocator implements FileConfLocator {

    private String nameEnvitoment;


    /**
     * constructor with name enviroment path
     * @param nameEnvitoment
     */
    public FileConfByEnviromentLocator(String nameEnvitoment){
        this.nameEnvitoment =   nameEnvitoment;
    }

    /**
     *
     * @param fileName name file
     * @param extension extension
     * @return  path by enviroment path
     */
    public String locate(String fileName, String extension) {
        String env = System.getenv(nameEnvitoment);
        String result ="";
        if (env!=null){
            result =  env+"/"+fileName+"."+extension;
        }
        return result;
    }
}