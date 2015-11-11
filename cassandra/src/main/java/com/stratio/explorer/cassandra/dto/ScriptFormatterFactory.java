package com.stratio.explorer.cassandra.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * create types of formatter .
 */
public class ScriptFormatterFactory {




    private static Map<String,ScriptFormatter> formmatters = new HashMap<String,ScriptFormatter> (){
        {
            put("CREATE",new ScriptCreateFormatter());
        }

    };

    /**
     * Obtain ScriptFormmater in base of Script type
     * @param script script
     * @return ScriptFormmater
     */
    public static ScriptFormatter getFormatterTo(String scriptType) {
        ScriptFormatter formmater = formmatters.get(scriptType.toUpperCase());
        if (formmater==null){
            formmater = new NoFormatter();
        }
        return formmater;
    }
}
