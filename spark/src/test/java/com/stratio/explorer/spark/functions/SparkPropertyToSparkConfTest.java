package com.stratio.explorer.spark.functions;



import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import org.apache.spark.SparkConf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


public class SparkPropertyToSparkConfTest {


    private Properties properties;
    private SparkPropertyToSparkConf transform;

    @Before
    public void setUp(){
        properties = new Properties();
        transform  = new SparkPropertyToSparkConf(properties);
    }


    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertyNotExis(){
        transform.transform("master.spark");
    }

    @Test(expected = MasterPropertyNotFilledException.class)
    public void whenPropertyIsNotfilled(){
        properties.put("master.spark", "");
        transform.transform("master.spark");
    }


    @Test
    public void whenPassAllChecked(){
        Assert.fail();
    }

}
