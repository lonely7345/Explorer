package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathOperations {

    private Path path;

    public PathOperations(Path path){
        this.path = path;
    }


    public boolean noFinishFolder(){
        return !ConstantsFolder.CT_NOT_EXIST_FOLDER.equals(path.toString());
    }

    public boolean notFileExist(){
        return Files.notExists(path);
    }

    public  void appendFolderToPath(String folder){

        if (path==null) {
            path = Paths.get(ConstantsFolder.CT_NOT_EXIST_FOLDER);
        }else {
            path = Paths.get(path.toString(), folder);
        }
    }

    public void goParent(){
        path = path.getParent().getParent();
    }

    public Path getPath(){
        return path;
    }
}
