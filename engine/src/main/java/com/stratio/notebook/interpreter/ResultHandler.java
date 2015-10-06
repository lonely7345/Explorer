package com.stratio.notebook.interpreter;

import com.stratio.notebook.notebook.Paragraph;

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
