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
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.is;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;


@RunWith(PowerMockRunner.class)
@PrepareForTest( { FileConfByEnviromentLocator.class })
public class FileConfByEnviromentLocatorTest {

    private  FileConfByEnviromentLocator locator;
    private String CT_NAME_ENVIROMENT_PATH ="CT_NAME_ENVIROMENT_PATH";



    @Before
    public void setUp(){

        locator =   new FileConfByEnviromentLocator(CT_NAME_ENVIROMENT_PATH);
    }



    @Test
    public void whenEnviromentPathIsEmpty(){
        String fileName="filename";
        String extension ="conf";
        assertThat(locator.locate(fileName,extension),isEmptyString());
    }


    @Test
    public void whenEnviromentPathExist(){
        String fileName="filename";
        String extension = "conf";
        String valueEnviroment ="valueEnviroment";
        mockStatic(System.class);
        expect(System.getenv(CT_NAME_ENVIROMENT_PATH)).andReturn(valueEnviroment);
        replayAll();
        assertThat(locator.locate(fileName, extension),is(valueEnviroment+"/"+fileName+"."+extension));
    }
}
