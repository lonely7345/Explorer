package com.stratio.explorer.cassandra.gateways;

import org.junit.Test;

import com.stratio.explorer.reader.PropertiesReader;

/**
 * Created by afidalgo on 4/11/15.
 */
public class CassandraRealDriverTest {


    @Test
    public void whenUseDescribe(){
        CassandraDriver driver = new CassandraDriver(new CassandraSession());
        driver.getConnector().loadConfiguration(new PropertiesReader().readConfigFrom("cassandra"));
        driver.executeCommand("use system");
        driver.executeCommand("SELECT * FROM local");
    }
}
