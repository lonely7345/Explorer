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


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.stratio.notebook.doubles.TransformFunctionDouble;
import com.stratio.notebook.doubles.TypeTestOneDouble;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by afidalgo on 14/10/15.
 */
public class FunctionalListTest {



    private FunctionalList <String,TypeTestOneDouble>functionalList;
    private List<String> entryList;


    @Before
    public void setUp(){
        entryList = new ArrayList<>();
        functionalList = new FunctionalList<String,TypeTestOneDouble>(entryList);
    }


    @Test
    public void whenCallMapFunctionTransformImputTypeInOutputType(){
        String first ="first";
        entryList.add(first);
        List<TypeTestOneDouble> resultList = functionalList.map(new TransformFunctionDouble());
        assertThat(resultList.size(),is(1));
        assertThat(resultList.get(0).entry(),is(first));
    }
}
