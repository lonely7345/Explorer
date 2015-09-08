package com.stratio.notebook.cassandra.dto;

import com.stratio.notebook.cassandra.models.TableRow;
import org.json.JSONObject;


public interface RowToJsonTransformer<K> {

     JSONObject toJSON(TableRow<K> row);

}
