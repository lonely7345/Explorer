/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.notebook.lists;


import java.util.ArrayList;
import java.util.List;

import com.stratio.notebook.functions.TransformFunction;

public class FunctionalList <EntryType,OutputType>{

    private List<EntryType> entryList;

    /**
     * Contructor
     * @param entryList list to transform in functionalList
     */
    public FunctionalList(List<EntryType> entryList){
         this.entryList = entryList;
    }

    public List<OutputType> map(TransformFunction<EntryType,OutputType> function) {

        List<OutputType> resultList = new ArrayList<>();
        for (int index = 0;index<entryList.size();index++){
            resultList.add(function.transform(entryList.get(index)));
        }
        return resultList;
    }
}
