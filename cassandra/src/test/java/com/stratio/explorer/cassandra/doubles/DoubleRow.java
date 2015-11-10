/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
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
