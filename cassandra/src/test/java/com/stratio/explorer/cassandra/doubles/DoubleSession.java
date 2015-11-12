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



    public Session mockSessionWithData(){
        Session session = mock(Session.class);
        expect(session.execute("select * from mytable WHERE id='myKey01'")).andStubReturn(mockResultSet(mockRows(),mockColumnDefinions()));
        replay(session);
        return session;
    }


    private ResultSet mockResultSet(List<Row> rows,ColumnDefinitions columDefiniton){
        ResultSet resultSet = mock(ResultSet.class);
        ColumnDefinitions.Definition mockDefinition = new DoubleDefinition().buildDefinitionWithName("");
        expect(resultSet.getColumnDefinitions()).andStubReturn(columDefiniton);
        expect(resultSet.all()).andStubReturn(rows);
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

    public Session mockSessionWithOutData(){
        Session session = mock(Session.class);
        expect(session.execute("USE DEMO")).andStubReturn(mockResultSet(new ArrayList<Row>(),mockColumnDefinionsWithoutData()));
        replay(session);
        return session;
    }

    private ColumnDefinitions mockColumnDefinionsWithoutData(){
        ColumnDefinitions columnDefinions = mock(ColumnDefinitions.class);
        List<ColumnDefinitions.Definition> columnDefinitions = new ArrayList<>();
        expect(columnDefinions.asList()).andStubReturn(columnDefinitions);
        replay(columnDefinions);
        return columnDefinions;
    }
}
