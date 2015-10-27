package com.stratio.explorer.gateways;

import java.util.Properties;


/**
 * Created by afidalgo on 15/10/15.
 */
public interface Connector<TypeConnector> {


    /**
     * Load configuration from Properties
     * @param properties with configuration
     * @return instance of Connector
     */
     Connector loadConfiguration(Properties properties);

    /**
     * Obtain connector to any module
     * @return connector with typeConnector
     */
     TypeConnector getConnector();
}
