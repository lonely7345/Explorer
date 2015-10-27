package com.stratio.explorer.cassandra.gateways;


import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;



/**
 * Created by afidalgo on 15/10/15.
 */
//TODO : CHNAGE THIS TEST
public class CassandraSessionTest {

    private Connector session;
    private Properties properties;

    @Before
    public void setUp(){
        session = new CassandraSession();
        properties = new Properties();
    }


    @Test(expected = NotPropertyFoundException.class)
    public void whenPropertiesNotExist(){

         session.loadConfiguration(properties);
    }

  /*  @Test (expected = NotPropertyFoundException.class)
    public void whenPortIsEmpty(){
        properties.put(StringConstants.HOST, "127.0.1.1");
        session.loadConfiguration(properties);
    }


    @Test (expected = NotPropertyFoundException.class)
    public void whenHostIsEmpty(){
        properties.put(StringConstants.PORT, "123");
        session.loadConfiguration(properties);
    }*/
}
