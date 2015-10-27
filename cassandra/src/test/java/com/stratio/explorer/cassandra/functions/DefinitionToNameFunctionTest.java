package com.stratio.explorer.cassandra.functions;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import com.stratio.explorer.cassandra.doubles.DoubleDefinition;
import org.junit.Test;


/**
 * Created by afidalgo on 14/10/15.
 */


public class DefinitionToNameFunctionTest {




    @Test public void whenCallTransformFunction(){
        String name ="";
        DefinitionToNameFunction function = new DefinitionToNameFunction();
        assertThat(function.transform(new DoubleDefinition().buildDefinitionWithName(name)), is(name));
    }
}
