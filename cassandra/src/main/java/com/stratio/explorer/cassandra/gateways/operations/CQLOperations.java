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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.SyntaxError;
import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.explorer.cassandra.functions.DefinitionToNameFunction;
import com.stratio.explorer.cassandra.functions.RowToRowDataFunction;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.lists.FunctionalList;

/**
 * Execute standard CQL operations
 */
public class CQLOperations implements CassandraOperation {

    private Logger logger = LoggerFactory.getLogger(CQLOperations.class);


    /**
     * Execute standard CQL operations .
     * @param session
     * @param shCQLcommand
     * @return
     */
    @Override
    public Table execute(Session session, String shCQLcommand) {
        try {

            String[] commandSplit = shCQLcommand.split(";");
            //ResultSet rs = session.execute(shCQLcommand);
            //List<String> header = header(rs.getColumnDefinitions());
            //List<RowData> rows = new FunctionalList<Row, RowData>(rs.all()).map(new RowToRowDataFunction(header));

            List<RowData>rows=null;
            List<String> header=null;
            boolean moreOneCommand=false;

            for (int i=0;i<commandSplit.length;i++) {
                    logger.info("Commando: "+commandSplit[i]);
                    String command = commandSplit[i].replaceAll("\\n","").trim().concat(";");
                    ResultSet rs = session.execute(command);
                    header = header(rs.getColumnDefinitions());
                    rows = new FunctionalList<Row, RowData>(rs.all()).map(new RowToRowDataFunction(header));
                    if (i>0) {
                        moreOneCommand = true;
                    }
            }

            return new Table(header, appendOperationOkIfEmpty(rows, header, moreOneCommand));

        } catch (SyntaxError | InvalidQueryException e) {
            //String errorMessage = "  Query to execute in cassandra database is not correct ";
            String errorMessage = e.getMessage();
            logger.error(errorMessage);
            throw new CassandraInterpreterException(e, errorMessage);
        } catch (DriverException e) {
            logger.error(e.getMessage());
            throw new CassandraInterpreterException(e, e.getMessage());
        }
    }

    private List<RowData> appendOperationOkIfEmpty(List<RowData> rows,List<String> header, boolean moreOneCommand){
        List<CellData> cells = new ArrayList<>();
        if (moreOneCommand){
            cells.add(new CellData(StringConstants.OPERATIONS_OK));
        }else{
            cells.add(new CellData(StringConstants.OPERATION_OK));
        }

        if (rows.isEmpty() && header.isEmpty()){
            rows.add(new RowData(cells));
        }
        return rows;
    }


    private List<String> header(ColumnDefinitions definition){
        return new FunctionalList<ColumnDefinitions.Definition,String>(definition.asList()).map(new DefinitionToNameFunction());
    }
}
