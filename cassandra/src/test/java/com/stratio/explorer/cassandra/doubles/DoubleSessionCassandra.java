package com.stratio.explorer.cassandra.doubles;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.exceptions.ConnectionException;
import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;

import java.util.Properties;

/**
 * Created by afidalgo on 15/10/15.
 */
public class DoubleSessionCassandra implements Connector<Session> {

    private boolean isUpper;
    private boolean notProperty;

    public DoubleSessionCassandra(boolean isUpper,boolean notProperty){
        this.isUpper = isUpper;
        this.notProperty = notProperty;
    }


    @Override
    public Connector loadConfiguration(Properties properties) {
        if (notProperty)
            throw new NotPropertyFoundException(new Exception(),"");
        return null;
    }

    @Override
    public Session getConnector() {
        if (!isUpper)
            throw new ConnectionException(new Exception(),"exception");
        return new DoubleSession().mockSession();
    }
}
