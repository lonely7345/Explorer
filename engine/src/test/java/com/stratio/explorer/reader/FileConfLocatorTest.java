/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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
package com.stratio.explorer.reader;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by afidalgo on 26/10/15.
 */
public class FileConfLocatorTest {

    private FileConfLocator locator;

    @Before
    public void setUp(){
        locator = new FileConfLocator();
    }

    @Test
    public void whenFileExistInSameFolder(){
        String filePath = locator.locate("test_file.conf");
        assertTrue("when locate file then return path",filePath.endsWith("test-classes/test_file.conf"));
    }


    @Test
    public void whenFileExistIndistinctFolder(){
        String filePath = locator.locate("distinct_folder.conf");
        assertTrue("when locate file then return path", filePath.endsWith("test/java/com/stratio/explorer/reader/distinct_folder.conf"));
    }


    @Test
    public void whenFileNotexist(){
        String filePath = locator.locate("NO_FILE_SKHASGFTF.conf");
        assertThat("When file not exist then return emptyString", filePath,is(""));
    }
}
