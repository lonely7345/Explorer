package com.stratio.explorer.cassandra.dto;


import com.stratio.explorer.cassandra.constants.StringConstants;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import com.stratio.explorer.cassandra.utils.ListUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class TableDTOSelectorTest {


    private TableDTOSelector selector;

    @Before
    public void setUp(){
        selector = new TableDTOSelector();
    }



    @Test
    public void whenTableNotHaveHeader(){

        Table table = new Table(new ListUtils<String>().buildList(),new ArrayList<RowData>());
        assertThat("return text",selector.toDTO(table),startsWith(StringConstants.TYPE_TEXT));

    }


    @Test
    public void whenTableHaveHeader(){
        String anyCellHeader = "anyCellHeader";
        Table table = new Table(new ListUtils<String>().buildList(anyCellHeader),build());
        assertThat("return table",selector.toDTO(table),startsWith(StringConstants.TYPE_TABLE));
    }

    private List<RowData> build(){
       List<RowData> rows = new ArrayList<RowData>();
       List<CellData> cells =new ArrayList<>();
       CellData cellData = new CellData("any");
       cells.add(cellData);
       rows.add(new RowData(cells));
       return rows;
    }
}
