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

package com.stratio.explorer.cassandra.functions;

import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.cassandra.exceptions.NotValidPortException;
import com.stratio.explorer.cassandra.gateways.CassandraDriver;
import com.stratio.explorer.functions.TransformFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.net.InetSocketAddress;
import java.util.Properties;


public class CassandraPropertyToInetSocket implements TransformFunction<String, InetSocketAddress> {

    private Properties properties;
    private Logger logger = LoggerFactory.getLogger(CassandraPropertyToInetSocket.class);

    /**
     * Constructor.
     * @param properties properties to load
     */
    public CassandraPropertyToInetSocket(Properties properties){
        this.properties = properties;
    }

    /**
     * Tranform list propertyValue of propertieName to InetSocketAddress.
     * @param propertieName key Of properties
     * @return InetSocketAddress
     */
    @Override
    public InetSocketAddress transform(String propertieName) {
        try {
            String concatPointWithPort = (String) properties.get(propertieName);
            String[] hostAndPort = concatPointWithPort.split(":");
            return new InetSocketAddress(hostAndPort[0], Integer.valueOf(hostAndPort[1]));
        }catch (NumberFormatException e){
            String message ="Port is not valid in cassandra properies "+propertieName;
            logger.error(message);
            throw new NotValidPortException(e, message);
        }catch (IndexOutOfBoundsException e){
            String message = "Any cassandra property is not filled ";
            logger.error(message);
            throw new NotPropertyFoundException(e,message);
        }
    }
}
