package com.stratio.explorer.cassandra.doubles;

import com.datastax.driver.core.Row;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;

/**
 * Created by afidalgo on 14/10/15.
 */
public class DoubleRow {



    private String [] headers;

    public DoubleRow (String... header){
        this.headers = header;
    }

    public Row bildRow(){
        Row row = mock(Row.class);
        for (String header:headers) {
            expect(row.getObject(header)).andStubReturn(header);
        }
        replay(row);
        return row;
    }
}
