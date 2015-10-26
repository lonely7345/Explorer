package com.stratio.explorer.cassandra.gateways;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.exceptions.ConnectionException;
import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by afidalgo on 15/10/15.
 */
//TODO :  THIS CLASS ONLY TEST WITH REAL OR EMBBEDED CASSANDRA
public class CassandraSession implements Connector<Session> {



    private Logger logger = LoggerFactory.getLogger(CassandraDriver.class);

    private Session session;
    private int port = 0;
    private String host = "";
    private boolean isNewConfiguration=true;

    /**
     * Load configuration to Cassandra DataBase
     * @param properties with configuration
     * @return instance of CassandraSession
     */
    @Override
    public Connector loadConfiguration(Properties properties) {
        try {
            buildProperties(properties);
            return this;
        }catch (NumberFormatException e){
           String errorMessage = " Port property is not filled";
           logger.error(errorMessage);
           throw  new NotPropertyFoundException(e,errorMessage);
        }
    }

    public void buildProperties(Properties properties){
        int port = Integer.valueOf(properties.getProperty(StringConstants.PORT));
        String host =properties.getProperty(StringConstants.HOST);;
        if (host==null ){
            String errorMessage = " Host property is not filled";
            logger.error(errorMessage);
            throw  new NotPropertyFoundException(new Exception(),errorMessage);
        }

        if (port!=this.port || !host.equals(this.host)){
            this.port = port;
            this.host = host;
            this.isNewConfiguration = true;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Session getConnector() {
        try {
            if (isNewConfiguration){
                Cluster cluster = Cluster.builder().addContactPoint(host).withPort(port).build();
                session = cluster.connect();
                isNewConfiguration = false;
            }
            return session;
        }catch (NoHostAvailableException e ){
            String errorMessage ="  Cassandra database is not avalaible ";
            logger.error(errorMessage);
            throw new ConnectionException(e,errorMessage);
        }catch (RuntimeException e){
            String errorMessage ="  Cassandra database is not avalaible ";
            logger.error(errorMessage);
            throw new ConnectionException(e,errorMessage);
        }
    }
}
