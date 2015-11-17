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
package com.stratio.explorer.gateways;

import com.stratio.explorer.checks.CheckerCollection;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.CollectionsComparator;
import com.stratio.explorer.lists.FunctionalList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * this class must create a connector of typeConnector
 */
public class ConnectorCreator<TypeConnector> {


    private Logger logger = LoggerFactory.getLogger(ConnectorCreator.class);
    private ConnectorCreatorModel connectorCreatorModel = new ConnectorCreatorModel();

    private CheckerCollection checkerCollection;

    private CollectionsComparator<TypeConnector> comparator ;


    /**
     * Constructor with comparator and error message
     * @param comparator
     * @param errorMessage
     */
    public ConnectorCreator(CollectionsComparator<TypeConnector> comparator,String errorMessage){
        this.comparator = comparator;
        checkerCollection = new CheckerCollection<TypeConnector>(errorMessage);
    }


    /**
     * Read properties to Cassandra Database .
     * @param properties properties with (anyValue)->host:port estructure
     * @return Collection of InetSocketAddress
     */
    public void buildConnections(List<String> keysToInspect ,TransformFunction<String, TypeConnector> transformFunction) {
        Collection<TypeConnector> conectors =
                new FunctionalList<String,TypeConnector>(keysToInspect).map(transformFunction);
        checkerCollection.checkIsCollectionISNotEmpty(conectors);
        fillAttributtes(conectors);
    }

    private void fillAttributtes(Collection<TypeConnector> conectors){
        if (comparator.notEquals(conectors, connectorCreatorModel.getConnectors())){
            connectorCreatorModel.setIsNewConnector(true);
            connectorCreatorModel.setConectorList(new ArrayList<>(conectors));
        }
    }

    /**
     * Obtain last loaded connectionsAndHost .
     * @return last loaded connectionsAndHost
     */
    public Collection<TypeConnector> getConnections(){
        return connectorCreatorModel.getConnectors();
    }

    /**
     * Check if last loaded have new properties.
     * @return true if last Loaded has new properties false in other case
     */
    public boolean isNewConnexionLoaded() {
        return connectorCreatorModel.isNewConnectorLoaded();
    }

    /**
     * Change value to check if is new connection.
     * @param isNew new value
     */
    public void setNewConnection(boolean isNew) {
        connectorCreatorModel.setIsNewConnector(isNew);
    }
}
