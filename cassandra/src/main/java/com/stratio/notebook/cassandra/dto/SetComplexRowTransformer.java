package com.stratio.notebook.cassandra.dto;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;

import java.util.List;


public class SetComplexRowTransformer implements RowToJsonTransformer<List<TableRow>>{

    @Override
    public JSONObject toJSON(TableRow<List<TableRow>> row) {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, new TableRowsToJsonTransformer().toJSON(row.getValue()));
        return jsonObject;
    }

}
