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
package com.stratio.explorer.cassandra.dto;

/**
 * Format script Create to visible DTO .
 */
public class ScriptCreateFormatter implements ScriptFormatter {


    private final String FOUR_WHITE_SPACE ="    ";
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Format script Create to visible DTO .
     * @param script to format.
     * @return formatted script.
     */
    @Override
    public String format(String script) {
        String result = script.replaceAll(" AND ", LINE_SEPARATOR+FOUR_WHITE_SPACE+"AND ");
        result = result.replaceAll("\\(","("+LINE_SEPARATOR+FOUR_WHITE_SPACE);
        result = result.replaceAll("\\)",LINE_SEPARATOR+")");
        result = result.replaceAll(",",","+LINE_SEPARATOR+FOUR_WHITE_SPACE);
        return result;
    }
}
