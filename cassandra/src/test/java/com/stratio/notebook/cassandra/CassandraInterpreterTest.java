/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.notebook.cassandra;


import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.doubles.DoubleDriversBuilder;
import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.notebook.cassandra.models.CellData;
import com.stratio.notebook.cassandra.models.RowData;
import com.stratio.notebook.cassandra.models.Table;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;


public class CassandraInterpreterTest {


    private DoubleDriversBuilder selector;
    private Table initialDataInStore;
    private Interpreter interpreter;

    private final String NAME="name";
    private final String VALUE="value";


    @Before public void setUp(){
      /*  Properties properties = new Properties();*/
        System.setProperty(StringConstants.HOST, "127.0.0.1");
        System.setProperty(StringConstants.PORT, "9042");
        initialDataInStore = new Table();
        selector = new DoubleDriversBuilder();
        interpreter = new CassandraInterpreter(null);
    }

    @After public void teardown(){
        selector = null;
        initialDataInStore =null;
        CassandraInterpreterGateways.commandDriver = null;
        selector = null;
    }

    @Test public void whenCassandraDataBAseIsShutDownThenResultError(){
        selector.driverWithConnectionShutDown(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTOkeyspace_name.table_name;");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(Matchers.any(String.class)));
    }

    @Test public void whenCQLIsIncorrectResultShouldBeError(){
        selector.driverWithSyntaxError(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTOkeyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(Matchers.any(String.class)));
    }

    @Test public void whenCQLIsCorrectAndReturnOneResult(){
        initialDataInStore.addRow(buildRowData(new CellData(VALUE)));
        initialDataInStore.addHeaderParameter(NAME);
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("select * from demo.users");
        Assert.assertThat(result.code(), Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(NAME + "\n" + VALUE));
    }

    @Test public void whenCQULISCorrectAndHaveMoreResults(){
        initialDataInStore.addRow(buildRowData(new CellData(VALUE)));
        initialDataInStore.addRow(buildRowData(new CellData(VALUE)));
        initialDataInStore.addHeaderParameter(NAME);
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("select * from demo.users");
        Assert.assertThat(result.code(), Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(), Matchers.is(NAME + "\n" + VALUE + "\n" + VALUE));
    }

    @Test public void whenCqlIsCorrectAndNotHaceReturnedData(){
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("USE DEMO");
        Assert.assertThat(result.code(), Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(), Matchers.is(StringConstants.OPERATION_OK));
    }

    private RowData buildRowData(CellData... cells){
        RowData row = new RowData();
        for (CellData cell : cells)
            row.addCell(cell);
        return row;
    }

}