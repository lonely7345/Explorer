package com.stratio.explorer.cassandra.doubles;


import static org.easymock.EasyMock.mock;


import com.datastax.driver.core.ColumnDefinitions;

/**
 * Created by afidalgo on 14/10/15.
 */
public class DoubleDefinition {


    public  ColumnDefinitions.Definition buildDefinitionWithName(String nameHeader) {
        ColumnDefinitions.Definition definition = mock(ColumnDefinitions.Definition.class);
        return definition;
    }
}
