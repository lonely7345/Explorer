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

package com.stratio.explorer.checks;

import com.stratio.explorer.exceptions.NotPropertyFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CheckerCollectionTest {



    private Collection<Integer> colletion;
    private CheckerCollection<Integer> checkerCollection;

    @Before
    public void setUp(){
        colletion = new ArrayList<>();
        checkerCollection = new CheckerCollection<Integer>("message");

    }



    @Test(expected = NotPropertyFoundException.class)
    public void whenCollectionIsEmptyThrowError(){
        checkerCollection.checkIsCollectionISNotEmpty(colletion);
    }


    @Test
    public void whenCollectionIsNotEmpty(){
        try{
            colletion.add(Integer.valueOf(1));
            checkerCollection.checkIsCollectionISNotEmpty(colletion);
            assertTrue("mothod should not thorw exception ", true);
        }catch (NotPropertyFoundException e){
            fail("mothod should not thorw exception ");
        }
    }
}
