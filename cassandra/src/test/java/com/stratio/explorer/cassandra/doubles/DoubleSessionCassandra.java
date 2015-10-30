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
