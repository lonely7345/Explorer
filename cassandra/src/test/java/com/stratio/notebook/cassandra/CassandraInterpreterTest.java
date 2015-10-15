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

import java.util.ArrayList;
import java.util.List;


public class CassandraInterpreterTest {


    private DoubleDriversBuilder selector;
    private Table initialDataInStore;
    private List<String> header ;
    private List<RowData> rows;
    private Interpreter interpreter;

    private final String NAME="name";
    private final String VALUE="value";


    @Before public void setUp(){
        System.setProperty(StringConstants.HOST, "127.0.0.1");
        System.setProperty(StringConstants.PORT, "9042");
        header = new ArrayList<>();
        rows = new ArrayList<>();
        initialDataInStore = new Table(header,rows);
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
        rows.add(buildRowData(new CellData<>(VALUE)));
        header.add(NAME);
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("select * from demo.users");
        Assert.assertThat(result.code(), Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(NAME + "\n" + VALUE));
    }

    @Test public void whenCQULISCorrectAndHaveMoreResults(){
        rows.add(buildRowData(new CellData<>(VALUE)));
        rows.add(buildRowData(new CellData<>(VALUE)));
        header.add(NAME);
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


    @Test public void whenCallCompletion(){
        List<String> resultList =interpreter.completion("", 0);
        Assert.assertTrue(resultList.isEmpty());
    }

    @Test public void whenCallFormType(){
        Interpreter.FormType formType =interpreter.getFormType();
        Assert.assertThat(formType, Matchers.is(Interpreter.FormType.SIMPLE));

    }

    @Test public void whenCallGetValue(){
            Assert.assertEquals(interpreter.getValue("any"), null);

    }

    @Test public void whenFolderConfigurationNotFound(){
        selector.driverWithNotFoundException(initialDataInStore);
        InterpreterResult result =interpreter.interpret("USE DEMO");
        Assert.assertThat(result.code(), Matchers.is(InterpreterResult.Code.ERROR));
    }


    private RowData buildRowData(CellData... cells){
        List<CellData> cellsData = new ArrayList<>();
        for (CellData cell : cells)
            cellsData.add(cell);
        return new RowData(cellsData);
    }

}