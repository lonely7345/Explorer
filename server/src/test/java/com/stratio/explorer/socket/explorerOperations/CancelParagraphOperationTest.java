package com.stratio.explorer.socket.explorerOperations;


import org.junit.Before;
import org.junit.Test;


import static com.stratio.explorer.socket.explorerOperations.MocksToOperations.mockSocket;
import static com.stratio.explorer.socket.explorerOperations.MocksToOperations.mockNotebook;
import static com.stratio.explorer.socket.explorerOperations.MocksToOperations.mockMessage;


public class CancelParagraphOperationTest {

    private CancelParagraphOperation operation;


    @Before
    public void setUp(){
        operation = new CancelParagraphOperation();
    }



    @Test
    public void whenCallExecuteAndIdIsNull(){
        operation.execute(mockSocket(),mockNotebook(false),mockMessage(null));
    }


    @Test
    public void whenCallExecuteAndIdIsempty(){
        operation.execute(mockSocket(),mockNotebook(false),mockMessage(""));
    }


    @Test
    public void whenCallExecuteAndIdnotempty(){
        operation.execute(mockSocket(),mockNotebook(true),mockMessage("anyId"));
    }

}
