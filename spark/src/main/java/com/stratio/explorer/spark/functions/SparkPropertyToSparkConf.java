package com.stratio.explorer.spark.functions;


import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.spark.exception.MasterPropertyNotFilledException;
import org.apache.spark.SparkConf;

import java.util.Properties;

public class SparkPropertyToSparkConf implements TransformFunction<String,SparkConf> {

    private Properties properties;

    /**
     * Constructor with SparkContex configuration
     * @param properties
     */
    public SparkPropertyToSparkConf(Properties properties){
        this.properties = properties;
    }


    /**
     * Transform propertyName into file properties in SparkConf.
     * @param propertyName
     * @return SparkConf
     */
    @Override
    public SparkConf transform(String propertyName) {
         String valueProperty = (String)properties.get(propertyName);
         checkPropertyExist(valueProperty);
         checkPropertyIsNotempty(valueProperty);
         return null;
    }


    private void checkPropertyExist(String propertyValue){
        if (propertyValue==null) {
            throw new NotPropertyFoundException(new Exception(), "Property " + propertyValue + " no exist ");
        }
    }



    private void checkPropertyIsNotempty(String propertyValue){
        if (propertyValue.isEmpty())
            throw new MasterPropertyNotFilledException("Property "+propertyValue+ " not filled");
    }
}
