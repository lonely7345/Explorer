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
package com.stratio.explorer.converters;


import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


public class PropertiesToStringConverter {


    private String lineSeparator;

    public PropertiesToStringConverter(String lineSeparator){
        this.lineSeparator = lineSeparator;
    }

    /**
     * Transform porperties (key-value) separate by lineSeparator
     * @param properties
     * @return String with values
     */
    public String transform(Properties properties) {

        StringWriter    writer = new StringWriter();
        properties.list(new PrintWriter(writer));
        return deleteHeaderLine(writer.getBuffer().toString());
    }

    private String deleteHeaderLine(String string){
        String [] strings = string.split("\\n");
        List<String> withOutHeader =Arrays.asList(Arrays.copyOfRange(strings, 1, strings.length));
        Collections.reverse(withOutHeader);
        return StringUtils.join(withOutHeader,lineSeparator);

    }

}
