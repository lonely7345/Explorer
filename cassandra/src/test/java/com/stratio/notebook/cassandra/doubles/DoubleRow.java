/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.notebook.cassandra.doubles;

import com.datastax.driver.core.Row;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;

/**
 * Created by afidalgo on 14/10/15.
 */
public class DoubleRow {



    private String [] header;

    public DoubleRow (String... header){
        this.header = header;
    }

    public Row bildRow(){
        Row row = mock(Row.class);
        expect(row.getObject(header[0])).andStubReturn(header[0]);
        expect(row.getObject(header[1])).andStubReturn(header[1]);
        replay(row);
        return row;
    }
}
