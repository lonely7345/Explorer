package com.stratio.notebook.cassandra.dto;


import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonResultBuilder {


    public JSONObject buildSetSimple(TableRow<List<String>> row){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, row.getValue());
        return jsonObject;
    }

    public JSONObject buildSetComplex(TableRow<List<TableRow>> row){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, buildList(row.getValue()));
        return jsonObject;
    }

    private List<JSONObject> buildList(List<TableRow> rows){
       List<JSONObject> list = new ArrayList<>();
       for (TableRow row : rows){
           JSONObject json = new JSONObject();
           json.put(StringConstants.ATTRIBUTE_NAME,row.getName());
           json.put(StringConstants.ATTRIBUTE_TYPE,row.getType());
           json.put(StringConstants.ATTRIBUTE_VALUE,buildSimple((TableRow<String>) row.getValue()));
           list.add(json);
       }
       return list;
    }

    public JSONObject buildComplex(TableRow<TableRow<String>> row){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, buildSimple(row.getValue()));
        return jsonObject;
    }

    public JSONObject buildSimple(TableRow<String> row){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(StringConstants.ATTRIBUTE_NAME, row.getName());
        jsonObject.put(StringConstants.ATTRIBUTE_TYPE, row.getType());
        jsonObject.put(StringConstants.ATTRIBUTE_VALUE, row.getValue());
        return jsonObject;
    }
}
