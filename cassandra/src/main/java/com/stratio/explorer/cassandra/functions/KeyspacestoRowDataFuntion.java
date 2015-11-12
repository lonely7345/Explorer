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

package com.stratio.explorer.cassandra.functions;

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.functions.TransformFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform KeySpace to row Data
 */
public class KeyspacestoRowDataFuntion implements TransformFunction<KeyspaceMetadata,RowData>{



    /**
     * Transform KeyspaceMetadata to RowData
     * @param objetcToTransform input object
     * @return
     */
    @Override
    public RowData transform(KeyspaceMetadata keyspaceMetadata) {
        List<CellData> cells = new ArrayList<>();
        cells.add(new CellData(keyspaceMetadata.getName()));
        return new RowData(cells);
    }
}
