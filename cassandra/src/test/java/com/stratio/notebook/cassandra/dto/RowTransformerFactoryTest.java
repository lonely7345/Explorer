package com.stratio.notebook.cassandra.dto;


import com.stratio.notebook.cassandra.models.TableRow;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RowTransformerFactoryTest {


    private TableRowDoublesBuilder rowBuilder;
    private JsonResultBuilder jsonBuilder;
    private RowTransformersFactory factory;

    @Before
    public void setUp(){
        factory = new RowTransformersFactory();
        rowBuilder = new TableRowDoublesBuilder();
        jsonBuilder = new JsonResultBuilder();

    }

    @Test
    public void whenTableRowContainsOnlyTypeSimplex(){
        TableRow<String> row =rowBuilder.rowTypeSimple();
        RowToJsonTransformer transformer = factory.getTransformer(row.getType());
        Assert.assertThat(transformer.toJSON(row).toString(), Matchers.is(jsonBuilder.buildSimple(row).toString()));
    }

    @Test
    public void whenTableRowContainsTypeSetSimple(){
        TableRow<List<String>> row = rowBuilder.rowTypeSetSimple();
        RowToJsonTransformer transformer = factory.getTransformer(row.getType());
        Assert.assertThat(transformer.toJSON(row).toString(), Matchers.is(jsonBuilder.buildSetSimple(row).toString()));
    }

    @Test
    public void whenTableRowContainsTypeComplex(){
        TableRow<TableRow<String>> row = rowBuilder.rowTypeComplex();
        RowToJsonTransformer transformer = factory.getTransformer(row.getType());
        Assert.assertThat(transformer.toJSON(row).toString(), Matchers.is(jsonBuilder.buildComplex(row).toString()));
    }

    @Test
    public void whenTableRowContainTypeSetComplex(){
        TableRow row = rowBuilder.rowTypeSetComplex();
        RowToJsonTransformer transformer = factory.getTransformer(row.getType());
        Assert.assertThat(transformer.toJSON(row).toString(), Matchers.is(jsonBuilder.buildSetComplex(row).toString()));
    }
}
