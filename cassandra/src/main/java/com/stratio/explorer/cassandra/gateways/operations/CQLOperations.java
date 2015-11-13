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

package com.stratio.explorer.cassandra.gateways.operations;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
            ResultSet rs =session.execute(shCQLcommand);
            List<String> header = header(rs.getColumnDefinitions());
            List<RowData> rows = new FunctionalList<Row,RowData>(rs.all()).map(new RowToRowDataFunction(header));

            return new Table(header,appendOperationOkIfEmpty(rows,header));
        }catch (SyntaxError | InvalidQueryException e){
            String errorMessage = "  Query to execute in cassandra database is not correct ";
            logger.error(errorMessage);
            throw new CassandraInterpreterException(e,errorMessage);
        }
    }

    private List<RowData> appendOperationOkIfEmpty(List<RowData> rows,List<String> header){
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(StringConstants.OPERATION_OK));
        if (rows.isEmpty() && header.isEmpty()){
            rows.add(new RowData(cells));
        }
        return rows;
    }


    private List<String> header(ColumnDefinitions definition){
        return new FunctionalList<ColumnDefinitions.Definition,String>(definition.asList()).map(new DefinitionToNameFunction());
    }
}
