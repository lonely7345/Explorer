/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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
