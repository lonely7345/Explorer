package com.stratio.notebook.mongodb.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.*;
/**
 * Created by jmgomez on 11/09/15.
 */
@RunWith(PowerMockRunner.class)
public class CommandDeciderTest {

    @Test
    public void testDecedeCommandShowDBS() throws Exception {
        assertTrue("The command must be a MongoCommandExecutorShowDBS", CommandDecider.decideCommand("show dbs",null) instanceof MongoCommandExecutorShowDBS);

    }
}