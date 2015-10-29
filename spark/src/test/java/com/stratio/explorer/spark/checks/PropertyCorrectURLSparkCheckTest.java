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


package com.stratio.explorer.spark.checks;


import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class PropertyCorrectURLSparkCheckTest {


    private PropertyCorrectURLSparkCheck checker;

    @Before
    public void setUp(){
        checker = new PropertyCorrectURLSparkCheck("mesos","spark");
    }


    @Test(expected = MasterPropertyNotFilledException.class)
    public void whenNotContainsValidStartURL(){
        checker.check("mesos1://asassa,spark1://");
    }

    @Test(expected = MasterPropertyNotFilledException.class)
    public void whenContainAnyStartedURLCorrect(){
        checker.check("mesos1://asassa,spark://");
       assertTrue("Result shoudl be not throw exception",true);
    }



    @Test
    public void whenPropertyContainsAllInitialURL(){
       try {
           checker.check("mesos://asassa,spark://");
           assertTrue("Result shoudl be not throw exception",true);
       }catch (MasterPropertyNotFilledException e){
           fail("Result shoudl be not throw exception");
       }
    }
}
