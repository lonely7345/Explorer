package com.stratio.notebook.cassandra.dto;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DataTableDTO {


     public String toDTO(DataTable dataTable) {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put(StringConstants.ROWS, new TableRowsToJsonTransformer().toJSON(dataTable.rows()));
        return jsonObject.toString();
    }

}
