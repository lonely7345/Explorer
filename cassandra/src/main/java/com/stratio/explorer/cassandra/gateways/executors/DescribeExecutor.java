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

package com.stratio.explorer.cassandra.gateways.executors;


import com.datastax.driver.core.Metadata;
import com.stratio.explorer.cassandra.models.Table;
import java.util.List;

/**
 * Execute describe in cassandra
 */
public interface DescribeExecutor {


    /**
     * last param in Describe with three params
     * @param param
     */
    public void optionalParam(String param);

    /**
     * Execute Describe shCQL
     * @param metaData
     * @return  table with results
     */
    public Table execute(Metadata metaData);
}
