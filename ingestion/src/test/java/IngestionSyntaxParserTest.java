import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stratio.ingestion.utils.IngestionAgent;
import com.stratio.ingestion.utils.IngestionParserException;
import com.stratio.ingestion.utils.IngestionParserResult;
import com.stratio.ingestion.utils.IngestionSyntaxParser;

import junit.framework.Assert;

/**
 * Created by idiaz on 3/08/15.
 */
public class IngestionSyntaxParserTest {
    IngestionSyntaxParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new IngestionSyntaxParser();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseAgentStart() {
        String filepath = getClass().getResource("testFile.properties").getPath();
        String name = "agentName";
        int port = 1000;
        IngestionAgent agent = new IngestionAgent(name, filepath,port);
        IngestionParserResult expectedResult = new IngestionParserResult(IngestionSyntaxParser.Command.AGENT_START,
                agent);

        String command = "agent start --file ".concat(filepath).concat(" --port 1000");
        try {
            IngestionParserResult result = parser.parse(command);
            Assert.assertEquals(expectedResult,result);
        } catch (IngestionParserException e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void parseAgentStartWrongFile() {
        String command = "agent start --file /wrong/path/file  --port 1000";
        try {
            parser.parse(command);
            Assert.fail();
        } catch (IngestionParserException e) {
            Assert.assertNotNull(e);
        }
    }
}
