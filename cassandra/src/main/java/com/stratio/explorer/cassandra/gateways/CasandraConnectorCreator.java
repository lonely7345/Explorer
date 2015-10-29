/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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

import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.cassandra.functions.CassandraPropertyToInetSocket;
import com.stratio.explorer.lists.FunctionalList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;


/**
 * This class must create Cassandra Connections.
 */
public class CasandraConnectorCreator {

    private Logger logger = LoggerFactory.getLogger(CasandraConnectorCreator.class);
    private List<InetSocketAddress> contactPointWithPorts = new ArrayList<>();
    private boolean isNewConection=false;

    /**
     * Read properties to Cassandra Database .
     * @param properties properties with (anyValue)->host:port estructure
     * @return Collection of InetSocketAddress
     */
    public void buildConnections(Properties properties) {
        Collection<InetSocketAddress> localConcatpoint = extractConectionFrom(properties);
        fillAttributtes(localConcatpoint);
        if (localConcatpoint.isEmpty()){
            String errorMessage = " Host port property is not filled";
            logger.error(errorMessage);
            throw  new NotPropertyFoundException(new Exception(),errorMessage);
        }
    }

    private Collection<InetSocketAddress> extractConectionFrom(Properties properties){
        List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        FunctionalList<String,InetSocketAddress> functionalList = new FunctionalList<String,InetSocketAddress>(keys);
        return functionalList.map(new CassandraPropertyToInetSocket(properties));
    }

    private void fillAttributtes(Collection<InetSocketAddress> localConcatpoint){
        if (!contactPointWithPorts.containsAll(localConcatpoint) || !(localConcatpoint.size() == contactPointWithPorts.size())){
            isNewConection = true;
            contactPointWithPorts = new ArrayList<>(localConcatpoint);
        }
    }

    /**
     * Obtain last loaded connectionsAndHost .
     * @return last loaded connectionsAndHost
     */
    public Collection<InetSocketAddress> getConnections(){
        return contactPointWithPorts;
    }

    /**
     * Check if last loaded have new properties.
     * @return true if last Loaded has new properties false in other case
     */
    public boolean isNewConnexionLoaded() {
        return isNewConection;
    }

    /**
     * Change value to check if is new connection.
     * @param isNew new value
     */
    public void setNewConnection(boolean isNew) {
        isNewConection = isNew;
    }
}
