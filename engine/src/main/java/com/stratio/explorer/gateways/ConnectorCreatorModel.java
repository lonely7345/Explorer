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

package com.stratio.explorer.gateways;

import com.stratio.explorer.checks.CheckerCollection;
import com.stratio.explorer.functions.TransformFunction;
import com.stratio.explorer.lists.FunctionalList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Model taht encapsulate ConnectorCreator fields
 */
public class ConnectorCreatorModel<TypeConnector> {


    private boolean isNewConnector;
    private Collection<TypeConnector> connectorList = new ArrayList<>();



    /**
     * Return is  connection is new .
     * @return true if las connection is new
     */
    public boolean isNewConnectorLoaded() {
        return isNewConnector;
    }

    /**
     *  Return all configured connectors .
     * @return all configured connectors
     */
    public Collection<TypeConnector> getConnectors() {
        return connectorList;
    }


    /**
     * set new connector list .
     * @param connectorList
     */
    public void setConectorList(Collection<TypeConnector> connectorList ){
        this.connectorList = connectorList;
    }

    /**
     * set if connector is new .
     * @param newConnection
     */
    public void setIsNewConnector(boolean isNewConnection) {
        this.isNewConnector = isNewConnection;
    }

}
