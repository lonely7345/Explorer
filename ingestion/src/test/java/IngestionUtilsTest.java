/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import com.stratio.ingestion.utils.IngestionUtils;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by idiaz on 3/08/15.
 */
public class IngestionUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAgentNameTest() {
        String agentName = "agentName"; //@testFile.properties, first field
        try {

            String result = IngestionUtils.getAgentName(getClass().getResource("testFile.properties").getPath());
            Assert.assertEquals(agentName, result);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getChannelsStatusTest() {
        String json = "{\"SOURCE.seqGenSrc\":{\"OpenConnectionCount\":\"0\",\"Type\":\"SOURCE\",\"AppendBatchAcceptedCount\":\"0\",\"AppendBatchReceivedCount\":\"0\",\"EventAcceptedCount\":\"58792698\",\"StopTime\":\"0\",\"AppendReceivedCount\":\"0\",\"StartTime\":\"1438596662511\",\"EventReceivedCount\":\"0\",\"AppendAcceptedCount\":\"0\"},\"CHANNEL.memoryChannel\":{\"EventPutSuccessCount\":\"58792693\",\"ChannelFillPercentage\":\"99.0\",\"Type\":\"CHANNEL\",\"EventPutAttemptCount\":\"58792695\",\"ChannelSize\":\"99\",\"StopTime\":\"0\",\"StartTime\":\"1438596662508\",\"EventTakeSuccessCount\":\"58792593\",\"ChannelCapacity\":\"100\",\"EventTakeAttemptCount\":\"58792593\"}}";
        Map<String, String> channelsStatus = IngestionUtils.getChannelsStatus(json);
        Assert.assertEquals(channelsStatus.get("memoryChannel"), "99.0");
    }

    @Test
    public void getIngestionPropertiesFilesTest() {
        List<String> files = new ArrayList<>();
        String path = getClass().getResource("testFile.properties").getPath();
        String rootPath = path.substring(0, path.length() - "testFile.properties".length() - 1);

        IngestionUtils.getIngestionPropertiesFiles(files, rootPath);

        Assert.assertEquals(files.size(), 1);
        Assert.assertEquals(files.get(0), path);
    }
}
