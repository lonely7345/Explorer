package com.stratio.notebook.cassandra.dto;


import com.stratio.notebook.cassandra.constants.StringConstants;

import java.util.HashMap;
import java.util.Map;

public class RowTransformersFactory {


    private Map<String,RowToJsonTransformer> transformers = new HashMap<>();

    public RowTransformersFactory(){
        transformers.put(StringConstants.TYPE_SIMPLE,new SimpleRowTransformer());
        transformers.put(StringConstants.TYPE_SET_SIMPLE,new SimpleRowTransformer());
        transformers.put(StringConstants.TYPE_COMPLEX,new ComplexRowTransformer());
        transformers.put(StringConstants.TYPE_SET_COMPLEX,new SetComplexRowTransformer());
    }

    public RowToJsonTransformer getTransformer(String type){
        return transformers.get(type);
    }
}
