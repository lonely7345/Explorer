package com.stratio.explorer.cassandra.utils;

import com.stratio.explorer.cassandra.models.NameValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils to build Lists
 */
//
public class ListUtils<TypeList> {

    /**
     * build String list with params
     * @param params
     * @return
     */
    public List<TypeList> buildList(TypeList... params){
        List<TypeList> result = new ArrayList<>();
        for (TypeList param:params){
            result.add(param);
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
