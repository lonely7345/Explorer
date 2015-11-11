package com.stratio.explorer.cassandra.dto;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class ScriptCreateFormatterTest {



    private ScriptCreateFormatter scriptCreateFormatter;
    private String FOUR_WHITE_SPACE ="    ";

    @Before
    public void setUp(){
        scriptCreateFormatter = new ScriptCreateFormatter();
    }

    @Test
    public void whenScriptHaveReservedWordAnd(){
        String script = "CREATE TABLE DEMO.WITHFLOAT AND REPLICATION";
        String result= "CREATE TABLE DEMO.WITHFLOAT"+ System.getProperty("line.separator") +FOUR_WHITE_SPACE+"AND REPLICATION";
        assertThat(scriptCreateFormatter.format(script),is(result));
    }


    @Test
    public void whenScriptHaveOpenParenthesis(){
        String script = "CREATE TABLE DEMO.WITHFLOAT (any)";
        String result = "CREATE TABLE DEMO.WITHFLOAT (" +System.getProperty("line.separator")
                        +FOUR_WHITE_SPACE+"any"+System.getProperty("line.separator")+")";
        assertThat(scriptCreateFormatter.format(script),is(result));
    }


    @Test
    public void whenScriptHaveCommas(){
        String script = "CREATE TABLE DEMO.WITHFLOAT (any,other)";
        String result = "CREATE TABLE DEMO.WITHFLOAT (" +System.getProperty("line.separator")
                +FOUR_WHITE_SPACE+"any,"+System.getProperty("line.separator")+
                 FOUR_WHITE_SPACE+"other"+System.getProperty("line.separator")+")";
        assertThat(scriptCreateFormatter.format(script),is(result));
    }




}
