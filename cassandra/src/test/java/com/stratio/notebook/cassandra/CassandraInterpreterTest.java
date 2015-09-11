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




public class CassandraInterpreterTest {


    private DoubleDriversBuilder selector;
    private Table initialDataInStore;
    private Interpreter interpreter;

    private final String NAME="name";
    private final String VALUE="value";


    @Before public void setUp(){
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
        Assert.assertThat(result.message(),Matchers.is(NAME+"\n"+VALUE));
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
