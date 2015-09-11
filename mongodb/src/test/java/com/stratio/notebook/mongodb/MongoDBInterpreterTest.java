package com.stratio.notebook.mongodb;

import com.mongodb.MongoClient;
import com.stratio.notebook.interpreter.InterpreterResult;
import com.stratio.notebook.mongodb.command.CommandDecider;
import com.stratio.notebook.mongodb.command.MongoCommandExecutorShowDBS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
/**
 * Created by jmgomez on 11/09/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class, MongoDBInterpreter.class,MongoCommandExecutorShowDBS.class,CommandDecider.class})
@PowerMockIgnore("javax.management.*")
public class MongoDBInterpreterTest {

    public static final String COMMAND = "";
    public static final String RETURN_VALUE = "RETURN_VALUE";
    @Mock Logger log;
    MongoDBInterpreter mongoDBInterpreter = null;

    @Before
    public void setUp(){
        mongoDBInterpreter = new MongoDBInterpreter(null);
        Whitebox.setInternalState(mongoDBInterpreter, "LOG", log);
        when(log.isDebugEnabled()).thenReturn(true);
    }
    @Test
    public void testOpenSingleton() throws Exception {


        mongoDBInterpreter.open();
        MongoClient singleMongoClient = (MongoClient) Whitebox.getInternalState(mongoDBInterpreter, "mongoClient");

        mongoDBInterpreter.open();
        MongoClient mustBeSameMongoClient = (MongoClient) Whitebox.getInternalState(mongoDBInterpreter, "mongoClient");

        assertSame("The mongoClient must be singleton", singleMongoClient, mustBeSameMongoClient);

        verify(log,times(1)).info("Notebook has created a mongo connection.");
    }

    @Test
    public void testCloseAndopenToVerifyFlow() throws Exception {
        mongoDBInterpreter.open();
        MongoClient oneMongoClient = (MongoClient) Whitebox.getInternalState(mongoDBInterpreter, "mongoClient");
        assertNotNull("The connection must not be null", oneMongoClient);

        mongoDBInterpreter.close();
        assertNull("The connection must be null", (MongoClient) Whitebox.getInternalState(mongoDBInterpreter, "mongoClient"));


        mongoDBInterpreter.open();
        MongoClient otherMongoClient = (MongoClient) Whitebox.getInternalState(mongoDBInterpreter, "mongoClient");
        assertNotNull("The connection must not be null", otherMongoClient);

        assertNotSame("The connections must be different", oneMongoClient, otherMongoClient);

        verify(log, times(2)).info("Notebook has created a mongo connection.");
        verify(log, times(1)).info("Notebook has close a mongo connection.");
    }



    @Test
    public void testInterpret() throws Exception {

        MongoCommandExecutorShowDBS mongoCommandExecutor = mock(MongoCommandExecutorShowDBS.class);
        when(mongoCommandExecutor.execute(COMMAND)).thenReturn(RETURN_VALUE);

        mockStatic(CommandDecider.class);
        when(CommandDecider.decideCommand(eq(COMMAND), any(MongoClient.class))).thenReturn(mongoCommandExecutor);



        InterpreterResult interpreterResult = mongoDBInterpreter.interpret(COMMAND);

        assertEquals("The interpreterResult must return the correct command", interpreterResult, new InterpreterResult(InterpreterResult.Code.SUCCESS, RETURN_VALUE));

        verify(log, times(1)).debug("Start MongoInterpreter with " + COMMAND);

    }


}