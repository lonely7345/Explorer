package com.stratio.notebook.notebook.cassandra;


import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.notebook.cassandra.constants.MessageConstants;
import com.stratio.notebook.notebook.cassandra.doubles.InterpreterWithDoubleDriverSelector;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


//
//  TODO : INSERCIÓN CORRECTA (HECHO)
//  TODO : UPDATE CORRECTA
//  TODO : BORRADO CORRECTO
//  TODO : SELECT CORRECTO
//  TODO : CREACIÓN DE TABLA CORRECTA

/*******************************************************************************************/

//  TODO :INSERTCION EN TABLA QU NO EXISTE
//  TODO : UPDATADO
//  TODO : BORRADO
//  TODO : SELECT CON DATOS
//  TODO : SELECT SIN DATOS
//  TODO : CREACIÓN DE UNA TABLA
public class CassandraInterpreterTest {


    private InterpreterWithDoubleDriverSelector selector;


    @Before public void setUp(){
        selector = new  InterpreterWithDoubleDriverSelector();
    }

    @After public void teardown(){
        selector = null;
    }

    @Test public void whenCassandraIsShutdownResultShouldBeConnectionErrorMessage(){
        InterpreterResult result = selector.driverWithConnectionShutDown().interpret("anyCQL");
        Assert.assertThat(result.code(),Matchers.is(InterpreterResult.Code.ERROR));
        Assert.assertThat(result.message(),Matchers.is(MessageConstants.ERROR_SERVICE_NOT_UPPER));
    }

    @Test public void whenSyntaxIsIncorrectResultShouldBeSyntaxErrorMessage(){
        InterpreterResult result = selector.driverWithSyntaxError().interpret("anyCQLWithBadSyntax");
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
}
