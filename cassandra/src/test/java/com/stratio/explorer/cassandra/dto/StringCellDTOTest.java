package com.stratio.explorer.cassandra.dto;

import com.stratio.explorer.cassandra.models.CellData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class StringCellDTOTest {

    private String FOUR_WHITE_SPACE ="    ";
    private CellStringDTO dto;

    @Before
    public void setUp(){
        dto = new CellStringDTO();
    }

    @Test
    public void whenCellHaveAnScript(){
        String script = "CREATE TABLE DEMO.WITHFLOAT (any)";
        String result = "CREATE TABLE DEMO.WITHFLOAT (" +System.getProperty("line.separator")
                +FOUR_WHITE_SPACE+"any"+System.getProperty("line.separator")+")";
        assertThat("result should be Script formatted",dto.toDTO(new CellData(script)),is(result));
    }


    @Test
    public void whenCellNotHaveAndScript(){
        String script = "any";
        String result = "any";
        assertThat("result should be Script formatted",dto.toDTO(new CellData(script)),is(result));
    }
}
