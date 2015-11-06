package com.stratio.explorer.cassandra.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils to build Lists
 */
public class ListUtils {

    /**
     * build String list with params
     * @param params
     * @return
     */
    public static List<String> buildList(String... params){
        List<String> result = new ArrayList<>();
        for (String cad:params){
            result.add(cad);
        }
        return result;
    }
}
