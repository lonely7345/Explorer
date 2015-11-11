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

package com.stratio.explorer.cassandra.gateways.executors;

import com.stratio.explorer.cassandra.exceptions.NotDescribeOptionException;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * Created by afidalgo on 6/11/15.
 */
public class DescribeExecutorFactoryTest {



    @Test
    public void whenSelectedWordIsCorrect(){
        try{
            DescribeExecutor executor = DescribeExecutorFactory.select(DescribeClusterExecutor.WORD_SELECTOR);
            assertTrue("Executor should be recpovered",true);
        }catch (NotDescribeOptionException e){
            fail("Executor should be recpovered");
        }
    }

    @Test(expected = NotDescribeOptionException.class)
    public void whenSelectedwordIsNotCorrect(){
        DescribeExecutorFactory.select("");
    }
}
