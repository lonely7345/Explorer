/**
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

package com.stratio.notebook.notebook.utils;


import com.stratio.notebook.notebook.form.Input;

import java.util.HashMap;
import java.util.Map;

//TODO : surely this object will be removed
public class ParamsBuilder {



    public static Map<String,Object> buildParamsByExpectedInput(InputExpectedValues expectedvalues){
           Input input = new Input(expectedvalues.name, expectedvalues.displayName, expectedvalues.type,
                                   expectedvalues.defaultValue, expectedvalues.options, expectedvalues.hidden);
           Map<String,Object> params = new HashMap<>();
           params.put(expectedvalues.name,input);
           return params;
    }
}
