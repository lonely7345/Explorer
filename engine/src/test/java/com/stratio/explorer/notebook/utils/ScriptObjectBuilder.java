package com.stratio.explorer.notebook.utils;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ScriptObjectBuilder {


    private String simbolSeparator;

    public ScriptObjectBuilder(String simbolSeparator){
        this.simbolSeparator = simbolSeparator;
    }

    public String buildNotHiddenWith(KeyValue... keyValues){
        String open = "{",close = "}";
        List <String >listKeyValue = new ArrayList<>();
        for (KeyValue keyValue:keyValues){
            listKeyValue.add(keyValue.toStringSeparateBysimbol(simbolSeparator));
        }
        return open+StringUtils.join(listKeyValue,",")+close;
    }

}
