package com.stratio.notebook.cassandra;


import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.cassandra.constants.MessageConstants;
import com.stratio.notebook.cassandra.doubles.InterpreterWithDoubleDriverSelector;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CassandraInterpreterTest {


    private InterpreterWithDoubleDriverSelector selector;


    @Before public void setUp(){
        selector = new  InterpreterWithDoubleDriverSelector();
    }

    @After public void teardown(){
        selector = null;
    }

    @Test public void whenCassandraDataBAseIsShutDownThenResultError(){
        InterpreterResult result = selector.driverWithConnectionShutDown().interpret("INSERT INTOkeyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(Matchers.any(String.class)));

    }

    @Test public void whenCQLIsIncorrectResultShouldBeError(){
        InterpreterResult result = selector.driverWithSyntaxError().interpret("INSERT INTOkeyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(Matchers.any(String.class)));
    }



    /*@Test public void whenSyntaxIsIncorrectResultShouldBeSyntaxErrorMessage(){
        InterpreterResult result = selector.driverWithSyntaxError().interpret("INSERT INTO-keyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(MessageConstants.ERROR_CQL_SYNTAX));
    }

    @Test public void whenInsertIsCorrectResultShouldBeMessageWithInsertOk(){
        InterpreterResult result = selector.driverWithCorrectInset().interpret("INSERT INTO keyspace_name.table_name ");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(MessageConstants.INSERT_CORRECT));
    }

    @Test public void whenUpdateIsCorrectResultShouldBeMessageWithUpdateOk(){
        InterpreterResult result = selector.driverWithCorrectInset().interpret("UPDATE keyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(MessageConstants.UPDATE_CORRECT));
    }

    @Test public void whenDeleteIsCorrectResultShouldBeMessageWithDeleteOK(){
        InterpreterResult result = selector.driverWithCorrectInset().interpret("DELETE keyspace_name.table_name");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.SUCCESS));
        Assert.assertThat(result.message(),Matchers.is(MessageConstants.DELETE_CORRECT));
    }*/
}
