package com.stratio.explorer.lists;


import java.util.ArrayList;
import java.util.List;

import com.stratio.explorer.functions.TransformFunction;

public class FunctionalList <EntryType,OutputType>{

    private List<EntryType> entryList;

    /**
     * Contructor
     * @param entryList list to transform in functionalList
     */
    public FunctionalList(List<EntryType> entryList){
         this.entryList = entryList;
    }

    /**
     * Transforma list with EntryType elements y List OutputType elements
     * @param function transformfunction
     * @return List with OutputType elements
     */
    public List<OutputType> map(TransformFunction<EntryType,OutputType> function) {

        List<OutputType> resultList = new ArrayList<>();
        for (int index = 0;index<entryList.size();index++){
            resultList.add(function.transform(entryList.get(index)));
        }
        return resultList;
    }
}
