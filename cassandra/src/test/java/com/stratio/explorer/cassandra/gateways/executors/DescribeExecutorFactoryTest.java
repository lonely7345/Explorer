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
