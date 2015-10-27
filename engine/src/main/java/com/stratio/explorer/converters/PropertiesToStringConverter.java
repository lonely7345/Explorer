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
