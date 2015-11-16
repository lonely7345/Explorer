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
