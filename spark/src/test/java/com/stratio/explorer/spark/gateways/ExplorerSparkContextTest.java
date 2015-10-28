package com.stratio.explorer.spark.gateways;

import org.junit.Test;

import java.util.Properties;

/**
 * Created by afidalgo on 28/10/15.
 */
public class ExplorerSparkContextTest {



    @Test
    public void whenPropertysparkMasterNotExist(){
        Properties properties = new Properties();
        ExplorerSparkContex sparkContex = null;
        sparkContex.loadfrom(properties);
    }
}
