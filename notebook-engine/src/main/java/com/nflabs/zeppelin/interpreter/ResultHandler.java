package com.nflabs.zeppelin.interpreter;

import com.nflabs.zeppelin.notebook.Paragraph;

public abstract class ResultHandler {
    protected Paragraph paragraph;

    protected Boolean isLastResult;
    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public Boolean isLastResult() {
        return isLastResult;
    }
}
