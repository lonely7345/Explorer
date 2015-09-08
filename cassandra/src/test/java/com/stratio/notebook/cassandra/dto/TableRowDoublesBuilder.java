package com.stratio.notebook.cassandra.dto;


import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.TableRow;

import java.util.ArrayList;
import java.util.List;

public class TableRowDoublesBuilder {


    public TableRow<List<String>> rowTypeSetSimple() {
        TableRow<List<String>> tableRow = new TableRow();
        tableRow.attributeName("anyName");
        tableRow.attributeValue(buildGenericListString());
        tableRow.attributeType(StringConstants.TYPE_SET_SIMPLE);
        return tableRow;
    }

    private List<String> buildGenericListString() {
        List<String> list = new ArrayList<>();
        list.add("anyValue");
        return list;
    }

    public TableRow<TableRow<String>> rowTypeComplex() {
        TableRow<TableRow<String>> tableRow = new TableRow();
        tableRow.attributeName("anyName");
        tableRow.attributeValue(rowTypeSimple());
        tableRow.attributeType(StringConstants.TYPE_COMPLEX);
        return tableRow;
    }

    public TableRow<String> rowTypeSimple() {
        TableRow<String> tableRow = new TableRow();
        tableRow.attributeName("anyName");
        tableRow.attributeValue("anyValue");
        tableRow.attributeType(StringConstants.TYPE_SIMPLE);
        return tableRow;
    }

    public TableRow<List<TableRow>> rowTypeSetComplex() {
        TableRow<List<TableRow>> tableRow = new TableRow();
        tableRow.attributeName("anyName");
        tableRow.attributeValue(buildListTableRows());
        tableRow.attributeType(StringConstants.TYPE_SET_COMPLEX);
        return tableRow;
    }

    private List<TableRow> buildListTableRows() {
        List<TableRow> list = new ArrayList<>();
        list.add(rowTypeComplex());
        return list;
    }
}
