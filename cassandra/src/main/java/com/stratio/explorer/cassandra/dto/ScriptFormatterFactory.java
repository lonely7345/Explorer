/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
