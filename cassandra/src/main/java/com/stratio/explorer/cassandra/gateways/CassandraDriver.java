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


import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.SyntaxError;
import com.stratio.explorer.cassandra.functions.DefinitionToNameFunction;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.explorer.cassandra.functions.RowToRowDataFunction;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.gateways.Connector;
import com.stratio.explorer.interpreter.InterpreterDriver;
import com.stratio.explorer.lists.FunctionalList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class is the driver to cassandra.
 */
public class CassandraDriver implements InterpreterDriver<Table> {

    /**
     * The Log.
     */
    private Logger logger = LoggerFactory.getLogger(CassandraDriver.class);

    private Connector<Session> cassandraSession;

    public CassandraDriver(Connector<Session> cassandraSession){
        this.cassandraSession = cassandraSession;
    }

    /**
     * Execute CQL command in Cassandra dataBase
     * @param command command to execute
     * @return Table with data
     */
    @Override public Table executeCommand(String command) {
        try {
            Session session = cassandraSession.getConnector();
            ResultSet rs =session.execute(command);
            List<String> header = header(rs.getColumnDefinitions());
            List<RowData> rows = new FunctionalList<Row,RowData> (rs.all()).map(new RowToRowDataFunction(header));
            return new Table(header,rows);
        }catch (SyntaxError | InvalidQueryException e){
            String errorMessage = "  Query to execute in cassandra database is not correct ";
            logger.error(errorMessage);
            throw new CassandraInterpreterException(e,errorMessage);
        }
    }

    private List<String> header(ColumnDefinitions definition){
        return new FunctionalList<ColumnDefinitions.Definition,String>(definition.asList()).map(new DefinitionToNameFunction());
    }

    /**
     *
     * @return Connector to dataBae Cassandra
     */
    @Override
    public Connector getConnector() {
        return cassandraSession;
    }
}