package com.stratio.explorer.cassandra.dto;


import org.testng.annotations.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class ScriptFormatterFactoryTest {


    @Test
    public void whenScriptTypeIsReservedWordCreate(){
        String scriptType ="CREATE";
        ScriptFormatter formatter =ScriptFormatterFactory.getFormatterTo(scriptType);
        assertThat("return create formmater",formatter,instanceOf(ScriptCreateFormatter.class));

    }


    @Test
    public void whenScriptTypeIsNotReservedWordCreate(){
        String scriptType ="SELECT";
        ScriptFormatter formatter =ScriptFormatterFactory.getFormatterTo(scriptType);
        assertThat("return no formatter",formatter,instanceOf(NoFormatter.class));
    }

}
