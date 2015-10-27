package com.stratio.ingestion.utils;

/**
 * Created by idiaz on 16/07/15.
 */
public class IngestionParserException extends Exception {
    public IngestionParserException(Throwable e) {
        super(e);
    }

    public IngestionParserException(String m) {
        super(m);
    }
}
