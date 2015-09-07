package com.stratio.notebook.cassandra;


import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.dto.DataTableDTO;
import com.stratio.notebook.cassandra.dto.JsonResultBuilder;
import com.stratio.notebook.cassandra.dto.TableRowDoublesBuilder;
import com.stratio.notebook.cassandra.gateways.CassandraInterpreterGateways;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.cassandra.models.TableRow;
import com.stratio.notebook.interpreter.Interpreter;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.cassandra.doubles.DoubleDriversBuilder;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class CassandraInterpreterTest {


    private DoubleDriversBuilder selector;
    private DataTable initialDataInStore;
    private Interpreter interpreter;
    private JsonResultBuilder jsonResultBuilder;
    private TableRowDoublesBuilder tableRowBuilder;


    @Before public void setUp(){
        initialDataInStore = new DataTable();
        selector = new DoubleDriversBuilder();
        interpreter = new CassandraInterpreter();
        jsonResultBuilder = new JsonResultBuilder();
        tableRowBuilder = new TableRowDoublesBuilder();
    }

    @After public void teardown(){
        selector = null;
        initialDataInStore =null;
        CassandraInterpreterGateways.commandDriver = null;
        selector = null;
        jsonResultBuilder = null;
        tableRowBuilder = null;

    }

    @Test public void whenCassandraDataBAseIsShutDownThenResultError(){
        selector.driverWithConnectionShutDown(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTOkeyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(Matchers.any(String.class)));
    }

    @Test public void whenCQLIsIncorrectResultShouldBeError(){
        selector.driverWithSyntaxError(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTOkeyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(Matchers.any(String.class)));
    }

    @Test public void whenCQLIsCorrectAndNoReturnResultsResultShouldBeAndObjectWithEmptyRows(){
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("INSERT INTO keyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(new DataTableDTO().toDTO(initialDataInStore)));
    }

    @Test public void whenCQLIsCorrectAndReturnSimplesDataResultShouldBeObjectWithSimpleRows(){

        TableRow row = tableRowBuilder.rowTypeSimple();
        initialDataInStore.addRow(row);
        selector.driverWithCorrectCQL(initialDataInStore);
        InterpreterResult result =interpreter.interpret("SELECT *  keyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(tableDataJson(initialDataInStore)));

    }

    private String tableDataJson(DataTable initialDataInStore){
        List<TableRow> rows = initialDataInStore.rows();
        List<JSONObject> listJson = new ArrayList<>();
        JSONObject json = new JSONObject();
        for (TableRow row:rows) {
            listJson.add(jsonResultBuilder.buildSimple(row));
        }
        json.put(StringConstants.ROWS,listJson);
        return json.toString();
    }
}
