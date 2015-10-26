package com.stratio.explorer.reader;

import com.stratio.explorer.functions.SearchFileFunction;
import com.stratio.explorer.functions.SearcherFunction;
import com.stratio.explorer.lists.FunctionalList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by afidalgo on 26/10/15.
 */
public class FileConfLocator {

    private File folder;
    /**
     * Constructor init with
     */
    public FileConfLocator(){
        try {
            URL url = getClass().getClassLoader().getResource(".");
            folder = new File(url.toURI().getPath());
        }catch (URISyntaxException e){

        }
    }


    /**
     * locate any file into tree folders
     * @param fileName file to search
     * @return String with path
     */
    public String locate(String fileName)  {

        FunctionalList<File,File> functional = new FunctionalList(new ArrayList<>(FileUtils.listFiles(folder, null, true)));
        List<File> files = functional.search(new SearchFileFunction(fileName));
        if (!files.isEmpty()){
            return files.get(0).getPath(); //TODO : REFACTOR
        }

        if (folder.getPath().endsWith("Explorer")) {
            return ""; //TODO : REFACTOR
        }
        folder = new File(folder.getParent());
        return locate(fileName);
    }

}
