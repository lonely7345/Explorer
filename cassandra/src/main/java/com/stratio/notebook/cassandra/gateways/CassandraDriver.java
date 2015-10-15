/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stratio.notebook.cassandra.gateways;


import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.SyntaxError;
import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.functions.DefinitionToNameFunction;
import com.stratio.notebook.cassandra.functions.RowToRowDataFunction;
import com.stratio.notebook.cassandra.models.CellData;
import com.stratio.notebook.cassandra.models.RowData;
import com.stratio.notebook.cassandra.models.Table;
import com.stratio.notebook.gateways.Connector;
import com.stratio.notebook.interpreter.InterpreterDriver;
import com.stratio.notebook.lists.FunctionalList;
import com.stratio.notebook.reader.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * This class is the driver to cassandra.
 */
public class CassandraDriver implements InterpreterDriver<Table> {

    /**
     * The Log.
     */
    private Logger logger = LoggerFactory.getLogger(CassandraDriver.class);

    private Connector<Session> cassandraSession;

    public CassandraDriver(Connector<Session>cassandraSession){
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
            List<RowData> rows = createRow(rs.all(), header);
            return new Table(header,rows);
        }catch (SyntaxError | InvalidQueryException e){
            String errorMessage = "  Query to execute in cassandra database is not correct ";
            logger.error(errorMessage);
            throw new CassandraInterpreterException(e,errorMessage);
        }
    }

    /**
     *
     * @return Connector to dataBae Cassandra
     */
    @Override
    public Connector getConnector() {
        return cassandraSession;
    }

    private List<String> header(ColumnDefinitions definition){
        FunctionalList<ColumnDefinitions.Definition,String> functionalList = new FunctionalList<>(definition.asList());
        return functionalList.map(new DefinitionToNameFunction());
    }

    private List<RowData> createRow(List<Row> rows ,List<String> headerRows){
        FunctionalList<Row,RowData>  functionalList = new FunctionalList<>(rows);
        return functionalList.map(new RowToRowDataFunction(headerRows));
    }
}