package com.stratio.explorer.lists;


import java.util.ArrayList;
import java.util.List;

import com.stratio.explorer.functions.SearcherFunction;
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

    /**
     * search All elements that complain  searchCondition
     * @param searchCondition  condition
     * @return List with all elements that complain
     */
    public List<EntryType> search(SearcherFunction<EntryType> searchCondition) {
        List<EntryType> resultList = new ArrayList<>();
        for (int index = 0;index<entryList.size();index++){
            if (searchCondition.isValid(entryList.get(index)))
                resultList.add(entryList.get(index));
        }
        return resultList;
    }
}
