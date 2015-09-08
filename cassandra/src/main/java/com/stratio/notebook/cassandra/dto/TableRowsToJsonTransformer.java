package com.stratio.notebook.cassandra.dto;


import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TableRowsToJsonTransformer {


    public List<JSONObject> toJSON(List<TableRow> rowValues){
        RowTransformersFactory factory = new RowTransformersFactory();
        List<JSONObject> listJSON = new ArrayList<>();
        for (TableRow row :rowValues) {
            RowToJsonTransformer rowTransformer = factory.getTransformer(row.getType());
            listJSON.add(rowTransformer.toJSON(row));
        }
        return listJSON;
    }
}
