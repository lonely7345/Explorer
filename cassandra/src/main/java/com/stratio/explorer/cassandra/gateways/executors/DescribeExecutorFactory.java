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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Build factory of describe executors.
 */
public class DescribeExecutorFactory {

    private static Logger logger = LoggerFactory.getLogger(DescribeExecutorFactory.class);

    private static final Map<String,DescribeExecutor> executors =
             new HashMap<String,DescribeExecutor>(){
                 {  put(DescribeClusterExecutor.WORD_SELECTOR,new DescribeClusterExecutor());
                    put(DescribeKeySpaceAnyExecutor.WORD_SELECTOR,new DescribeKeySpaceAnyExecutor());
                    put(DescribeKeyspacesExecutor.WORD_SELECTOR,new DescribeKeyspacesExecutor());
                    put(DescribeTablesExecutor.WORD_SELECTOR,new DescribeTablesExecutor());
                 }
             };


    /**
     *  Select type of describe executor depend of wordSelector
     * @param wordSelector
     * @return DescribeExecutor to type describe
     */
    public static DescribeExecutor select(String wordSelector){
        DescribeExecutor executor = executors.get(wordSelector.toUpperCase());
        if (executor==null){
            String message =wordSelector + " not is correct in describe ";
            logger.error(message);
            throw new NotDescribeOptionException(message);
        }
        return executor;
    }


}
