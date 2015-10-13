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
import com.stratio.notebook.cassandra.models.CellData;
import com.stratio.notebook.cassandra.models.RowData;
import com.stratio.notebook.cassandra.models.Table;
import com.stratio.notebook.interpreter.InterpreterDriver;
import com.stratio.notebook.reader.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Session session;
    private int port = 0;
    private String host = "";

    /**
     * Constructor.
     * @param properties the properties.
     */
    public CassandraDriver(){


    }


    //TODO : THIS METHOD WILL BE REMOVED , ONLY MUST READ WHEN FILE IS CHANGED
    /**
     *  Read configuration from fileName
     * @param fileName name file
     * @return this object
     */
    @Override
    public InterpreterDriver<Table> readConfigFromFile(String fileName) {
        Properties properties = new PropertiesReader().readConfigFrom(fileName);
        host = properties.getProperty(StringConstants.HOST);
        port = Integer.valueOf(properties.getProperty(StringConstants.PORT));
        return this;
    }


    @Override public  void connect() {
        try {
            if (session == null){
                Cluster cluster = Cluster.builder().addContactPoint(host).withPort(port).build();
                session = cluster.connect();
            }
        }catch (NoHostAvailableException e){
            String errorMessage ="  Cassandra database is not avalaible ";
            logger.error(errorMessage);
            throw new ConnectionException(e,errorMessage);
        }
    }



    @Override public Table executeCommand(String command) {
        try {
            ResultSet rs =session.execute(command);
            Table table = createTable(rs.getColumnDefinitions());
            Iterator<Row> iterator =rs.iterator();
            while (iterator.hasNext())
                table.addRow(createRow(iterator.next(),table.header()));
            return table;
        }catch (SyntaxError | InvalidQueryException e){
            String errorMessage = "  Query to execute in cassandra database is not correct ";
            logger.error(errorMessage);
            throw new CassandraInterpreterException(e,errorMessage);
        }
    }


    private Table createTable(ColumnDefinitions definition){
        List<ColumnDefinitions.Definition> definitions = definition.asList();
        Table table = new Table();
        for (ColumnDefinitions.Definition def : definitions)
            table.addHeaderParameter(def.getName());
        return table;
    }

    private RowData createRow(Row cassandraRow,List<String> headerRows){
        RowData rowData = new RowData();
        for (String headerRowName:headerRows)
            rowData.addCell(new CellData(cassandraRow.getObject(headerRowName)));
        return rowData;
    }

}