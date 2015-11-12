/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.stratio.ingestion.utils.IngestionAgent;
import com.stratio.ingestion.utils.IngestionParserException;
import com.stratio.ingestion.utils.IngestionParserResult;
import com.stratio.ingestion.utils.IngestionSyntaxParser;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        IngestionAgent agent = new IngestionAgent(name, filepath, port);
        IngestionParserResult expectedResult = new IngestionParserResult(IngestionSyntaxParser.Command.AGENT_START,
                agent);

        String command = "agent start --file ".concat(filepath).concat(" --port 1000");
        try {
            IngestionParserResult result = parser.parse(command);
            Assert.assertEquals("The two instances are the sames",expectedResult, result);
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
