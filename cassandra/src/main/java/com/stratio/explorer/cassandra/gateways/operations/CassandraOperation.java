package com.stratio.explorer.cassandra.gateways.operations;

import com.datastax.driver.core.Session;
import com.stratio.explorer.cassandra.models.Table;

/**
 * Execute operations Like Describe , select , etc ..
 */
public interface CassandraOperation {
    /**
     * Execute cassandra operation in Session
     * @param session
     * @param shCQLcommand
     * @return table
     */
    Table execute(Session session, String shCQLcommand);
}
