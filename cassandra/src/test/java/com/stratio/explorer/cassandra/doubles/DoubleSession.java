package com.stratio.explorer.cassandra.doubles;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;

import java.util.ArrayList;
import java.util.List;


public class DoubleSession {



    public Session mockSession(){
        Session session = mock(Session.class);
        expect(session.execute("select * from mytable WHERE id='myKey01'")).andStubReturn(mockResultSet());
        replay(session);
        return session;
    }


    private ResultSet mockResultSet(){
        ResultSet resultSet = mock(ResultSet.class);
        ColumnDefinitions.Definition mockDefinition = new DoubleDefinition().buildDefinitionWithName("");
        expect(resultSet.getColumnDefinitions()).andStubReturn(mockColumnDefinions());
        expect(resultSet.all()).andStubReturn(mockRows());
        replay(resultSet);
        return resultSet;
    }


    private ColumnDefinitions mockColumnDefinions(){
        ColumnDefinitions columnDefinions = mock(ColumnDefinitions.class);
        List<ColumnDefinitions.Definition> columnDefinitions = new ArrayList<>();
        columnDefinitions.add(new DoubleDefinition().buildDefinitionWithName(""));
        expect(columnDefinions.asList()).andStubReturn(columnDefinitions);
        replay(columnDefinions);
        return columnDefinions;
    }

    private List<Row> mockRows(){
        List<Row> rows = new ArrayList<>();
        rows.add(new DoubleRow("").bildRow());
        return rows;
    }
}
