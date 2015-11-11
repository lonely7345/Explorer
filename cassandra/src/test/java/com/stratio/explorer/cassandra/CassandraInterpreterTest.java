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
package com.stratio.explorer.cassandra;


import com.stratio.explorer.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.doubles.DoubleDriversBuilder;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.interpreter.Interpreter;
import com.stratio.explorer.interpreter.InterpreterResult;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.endsWith;


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
        Assert.assertThat("When cassandra is shutDown status is error", result.code(),is(InterpreterResult.Code.ERROR));
        Assert.assertThat("and return error message",result.message(),is(any(String.class)));
    }

    @Test public void whenCQLIsIncorrectResultShouldBeError(){
        selector.driverWithSyntaxError(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTOkeyspace_name.table_name");
        Assert.assertThat("When CQL is incorrect status is error", result.code(),is(InterpreterResult.Code.ERROR));
        Assert.assertThat("and return error message", result.message(),is(any(String.class)));
    }

    @Test public void whenCQLIsCorrectAndReturnOneResult(){
        rows.add(buildRowData(new CellData<>(VALUE)));
        header.add(NAME);
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("select * from demo.users");
        Assert.assertThat("When CQL is correct status is success",result.code(), is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat("And return objetc in text format", result.message(), is(NAME + "\n" + VALUE));
    }

    @Test public void whenCQULISCorrectAndHaveMoreResults(){
        rows.add(buildRowData(new CellData<>(VALUE)));
        rows.add(buildRowData(new CellData<>(VALUE)));
        header.add(NAME);
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("select * from demo.users");
        Assert.assertThat("When CQL is correct status is success", result.code(), is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat("And return objetc in text format", result.message(), is(NAME + "\n" + VALUE + "\n" + VALUE));
    }

    @Test public void whenCqlIsCorrectAndNotHaceReturnedData() {
        rows.add(buildRowData(new CellData<>(StringConstants.OPERATION_OK)));
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("USE DEMO");
        Assert.assertThat("When CQL is correct status is success", result.code(), is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat("And return objetc in text format", result.message(), endsWith(StringConstants.OPERATION_OK));
    }


    @Test public void whenPropertyNotFoundException(){
        selector.driverWithNotPropertFoundException(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTOkeyspace_name.table_name;");
        Assert.assertThat("When property not found return error", result.code(), is(InterpreterResult.Code.ERROR));
        Assert.assertThat("With message", result.message(), is(any(String.class)));
    }


    @Test public void whenCallCompletion(){
        List<String> resultList =interpreter.completion("", 0);
        Assert.assertTrue("Always retirn cero", resultList.isEmpty());
    }

    @Test public void whenCallFormType(){
        Interpreter.FormType formType =interpreter.getFormType();
        Assert.assertThat("Always return SIMPLE", formType, is(Interpreter.FormType.SIMPLE));
    }

    @Test public void whenCallGetValue(){
            Assert.assertEquals("Always return null", interpreter.getValue("any"), null);
    }


    private RowData buildRowData(CellData... cells){
        List<CellData> cellsData = new ArrayList<>();
        for (CellData cell : cells)
            cellsData.add(cell);
        return new RowData(cellsData);
    }

}