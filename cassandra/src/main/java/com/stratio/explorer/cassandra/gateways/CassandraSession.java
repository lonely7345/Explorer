/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.cassandra.gateways;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.stratio.explorer.cassandra.exceptions.ConnectionException;
import com.stratio.explorer.cassandra.functions.CassandraPropertyToInetSocket;
import com.stratio.explorer.cassandra.lists.InetSocketAddressComparator;
import com.stratio.explorer.exceptions.NotPropertyFoundException;
import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.gateways.ConnectorCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class connect Session Cassandra
 */
//TODO :  THIS CLASS ONLY TEST WITH REAL OR EMBBEDED CASSANDRA
public class CassandraSession implements Connector<Session> {



    private Logger logger = LoggerFactory.getLogger(CassandraDriver.class);

    private Session session;
    private ConnectorCreator<InetSocketAddress> creator = new ConnectorCreator<InetSocketAddress>(new InetSocketAddressComparator(),"  Host port property is not filled ");

    /**
     * Load configuration to Cassandra DataBase
     * @param properties with configuration
     * @return instance of CassandraSession
     */
    @Override
    public Connector loadConfiguration(Properties properties) {
        try {
            List<String> keysToInspect =new ArrayList<>(properties.stringPropertyNames());
            creator.buildConnections(keysToInspect,new CassandraPropertyToInetSocket(properties));
            return this;
        }catch (NumberFormatException e){
           String errorMessage = " Port property is not filled";
           logger.error(errorMessage);
           throw  new NotPropertyFoundException(e,errorMessage);
        }
    }

    /**
     * Obtain sessionto connect cassandra
     * @return Session with cassandra
     */
    @Override
    public Session getConnector() {
        try {
            if (creator.isNewConnexionLoaded()){
                Cluster cluster = Cluster.builder().addContactPointsWithPorts(creator.getConnections()).build();
                session = cluster.connect();
                creator.setNewConnection(false);
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
