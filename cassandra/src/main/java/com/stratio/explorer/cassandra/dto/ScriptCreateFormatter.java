package com.stratio.explorer.cassandra.dto;

/**
 * Format script Create to visible DTO .
 */
public class ScriptCreateFormatter implements ScriptFormatter {


    private final String FOUR_WHITE_SPACE ="    ";


    /**
     * Format script Create to visible DTO .
     * @param script to format.
     * @return formatted script.
     */
    @Override
    public String format(String script) {
        String result = script.replaceAll(" AND ", System.getProperty("line.separator")+FOUR_WHITE_SPACE+"AND ");
        result = result.replaceAll("\\(","("+System.getProperty("line.separator")+FOUR_WHITE_SPACE);
        result = result.replaceAll("\\)",System.getProperty("line.separator")+")");
        result = result.replaceAll(",",","+System.getProperty("line.separator")+FOUR_WHITE_SPACE);
        return result;
    }
}
