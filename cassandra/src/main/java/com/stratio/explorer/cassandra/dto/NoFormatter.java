package com.stratio.explorer.cassandra.dto;

/**
 * Formatter that not apply any format.
 */
public class NoFormatter implements ScriptFormatter{

    /**
     * Return same Script
     * @param script to format
     * @return same Script
     */
    @Override
    public String format(String script) {
        return script;
    }
}
