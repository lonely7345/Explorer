package com.stratio.explorer.cassandra.utils;

import com.stratio.explorer.cassandra.models.NameValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils to build Lists
 */
//
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

    //TODO : CHANGE WITH OTHER
    public static List<NameValue> buildListNameValue(NameValue... nameValue){
        List<NameValue> result = new ArrayList<>();
        for (NameValue cad:nameValue){
            result.add(cad);
        }
        return result;
    }
}
