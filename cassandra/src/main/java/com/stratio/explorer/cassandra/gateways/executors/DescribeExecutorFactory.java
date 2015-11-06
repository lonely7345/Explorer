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
                 {  put(DescribeClusterExecutor.WORD_SELECTOR.toLowerCase(),new DescribeClusterExecutor());
                    put(DescribeKeySpaceAnyExecutor.WORD_SELECTOR.toLowerCase(),new DescribeKeySpaceAnyExecutor());
                    put(DescribeKeyspacesExecutor.WORD_SELECTOR.toLowerCase(),new DescribeKeyspacesExecutor());
                    put(DescribeTablesExecutor.WORD_SELECTOR.toLowerCase(),new DescribeTablesExecutor());
                 }
             };


    /**
     *  Select type of describe executor depend of wordSelector
     * @param wordSelector
     * @return DescribeExecutor to type describe
     */
    public static DescribeExecutor select(String wordSelector){
        DescribeExecutor executor = executors.get(wordSelector.toLowerCase());
        if (executor==null){
            String message =wordSelector + " not is correct in describe ";
            logger.error(message);
            throw new NotDescribeOptionException(message);
        }
        return executor;
    }


}
