package com.stratio.notebook.cassandra.dto;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;


public class SimpleRowTransformer implements  RowToJsonTransformer<TableRow<String>>{


    @Override
    public JSONObject toJSON(TableRow row) {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, row.getValue());
        return jsonObject;
    }
}
