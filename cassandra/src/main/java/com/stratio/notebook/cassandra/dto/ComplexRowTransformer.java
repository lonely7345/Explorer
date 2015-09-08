package com.stratio.notebook.cassandra.dto;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;


public class ComplexRowTransformer implements RowToJsonTransformer<TableRow<TableRow>>{


    @Override
    public JSONObject toJSON(TableRow<TableRow<TableRow>> row) {

        JSONObject jsonObject= new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, jsonOf(row.getValue()));
        return jsonObject;
    }

    private  JSONObject jsonOf(TableRow rowValue){
        RowTransformersFactory factory = new RowTransformersFactory();
        RowToJsonTransformer rowTransformer = factory.getTransformer(rowValue.getType());
        return rowTransformer.toJSON(rowValue);
    }
}
