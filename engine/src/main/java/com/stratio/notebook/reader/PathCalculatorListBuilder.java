/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
/**
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
package com.stratio.notebook.reader;


import com.stratio.notebook.conf.ConstantsFolder;

import java.util.ArrayList;
import java.util.List;

public class PathCalculatorListBuilder {

    /**
     * Build list with all pathCalculators
     * @return List with path Calculators
     */
    public static List<PathCalculator> build(){
        List<PathCalculator> listCalculators = new ArrayList<>();
        listCalculators.add(new NormalPathCalculator(ConstantsFolder.CT_FOLDER_CONFIGURATION));
        listCalculators.add(new EnvironmentPathCalculator(ConstantsFolder.CT_EXPLORER_CONF_DIR_ENV));
        return listCalculators;
    }
}
