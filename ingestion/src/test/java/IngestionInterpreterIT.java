import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import com.stratio.ingestion.IngestionInterpreter;

/**
 * Created by idiaz on 4/08/15.
 */
public class IngestionInterpreterIT {
    private IngestionInterpreter ingestion;
    @Before
    public void setUp() throws Exception {
        Properties p = new Properties();
        ingestion = new IngestionInterpreter(p);
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void interpretChannelStatus() {
//        InterpreterResult expectedResult = new InterpreterResult(InterpreterResult.Code.SUCCESS,"%text Channel "
//                + "memoryChannel:");
//        InterpreterResult result = ingestion.interpret("channel status --port 34545");
//
//        Assert.assertEquals(expectedResult.message(),result.message().substring(0,result.message().length()-6));
//        //removing the amount of channel expected
//
//    }
}
