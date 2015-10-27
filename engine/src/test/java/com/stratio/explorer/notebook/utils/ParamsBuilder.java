package com.stratio.explorer.notebook.utils;


import com.stratio.explorer.notebook.form.Input;

import java.util.HashMap;
import java.util.Map;


public class ParamsBuilder {



    public static Map<String,Object> buildParamsByExpectedInput(InputExpectedValues expectedvalues){
           Input input = new Input(expectedvalues.name, expectedvalues.displayName, expectedvalues.type,
                                   expectedvalues.defaultValue, expectedvalues.options, expectedvalues.hidden);
           Map<String,Object> params = new HashMap<>();
           params.put(expectedvalues.name,input);
           return params;
    }
}
