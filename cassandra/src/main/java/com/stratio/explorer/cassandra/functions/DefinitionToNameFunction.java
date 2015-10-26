package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.ColumnDefinitions;
import com.stratio.explorer.functions.TransformFunction;

/**
 * Created by afidalgo on 14/10/15.
 */
public class DefinitionToNameFunction implements TransformFunction<ColumnDefinitions.Definition,String> {


    /**
     * Extract name of columndefinions
     * @param definition Definition Objetc
     * @return name of definition
     */
    @Override
    public String transform(ColumnDefinitions.Definition definition) {
        String name = definition.getName();
        if (name==null)
            name="";
        return name;
    }
}
