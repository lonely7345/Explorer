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
package com.stratio.explorer.cassandra.gateways.operations;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.gateways.executors.*;
import com.stratio.explorer.cassandra.models.SHCQLOperation;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Execute Describe operations
 */
public class DecribeOperation implements CassandraOperation {



    /**
     * execute shCQLcoomand
     * @param Session session
     * @param shCQLcommand command shCQL
     * @return List string with result
     */
    @Override
    public Table execute(Session session, String shCQLcommand) {

        SHCQLOperation shCqlOperation = new SHCQLOperation(shCQLcommand);
        Cluster cluster = session.getCluster();
        DescribeExecutor executor = DescribeExecutorFactory.select(shCqlOperation.identifier());
        executor.optionalParam(shCqlOperation.optionalValue());
        return executor.execute(cluster.getMetadata());
    }
}
