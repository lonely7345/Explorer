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
package com.stratio.explorer.cassandra.functions;


import com.stratio.explorer.cassandra.exceptions.NotPropertyFoundException;
import com.stratio.explorer.cassandra.exceptions.NotValidPortException;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Properties;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CassandraPropertyToInetSocketTest {


    private CassandraPropertyToInetSocket toInetSocket;
    private Properties properties;


    @Before
    public void setUp(){
        properties = new Properties();
        toInetSocket = new CassandraPropertyToInetSocket(properties);
    }



    @Test
    public void whenPropertiesAreCorrect(){
        String host = "127.0.0.1";
        String port = "80";
        properties.put("prop", host + ":" + port);
        InetSocketAddress inetSocket = toInetSocket.transform("prop");
        assertThat("return host",inetSocket.getHostString(),is(host));
        assertThat("return port",inetSocket.getPort(),is(Integer.valueOf(port)));
    }

    @Test(expected = NotValidPortException.class)
    public void whenPortIsNotANumber(){
        String host = "127.0.0.1";
        String port = "80A";
        properties.put("prop", host + ":" + port);
        toInetSocket.transform("prop");
    }


    @Test(expected = NotPropertyFoundException.class)
    public void whenHostOrPortIsNotFilled(){
         String host = "127.0.0.1";
         String port = "";
         properties.put("prop", host);
         toInetSocket.transform("prop");
    }
}
