package com.stratio.explorer.cassandra.dto;

/**
 * Format to visualice Script
 */
public interface ScriptFormatter {

    /**
     * Format to visualice Script.
     * @param script to format
     * @return script formated
     */
    String format(String script);
}
