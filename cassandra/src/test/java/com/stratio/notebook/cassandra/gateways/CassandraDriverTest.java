package com.stratio.notebook.cassandra.gateways;

import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.models.RowData;
import com.stratio.notebook.cassandra.models.Table;
import org.junit.Test;
import org.junit.Assert;


import java.util.Properties;



public class CassandraDriverTest {

    private CassandraDriver driver;

    @Test
    public void whenClusterIsShutDown(){
        Properties properties = new Properties();
        properties.setProperty(StringConstants.HOST, "127.0.0.1");
        properties.setProperty(StringConstants.PORT, "9042");

        driver = new CassandraDriver(properties);
        driver.connect();

        Table dataRow =driver.executeCommand("SELECT * FROM DEMO.USERS;");
     /*   Assert.assertEquals(dataRow.cells().size(),3);
        Assert.assertEquals(dataRow.cells().get(0).getName(),"id");
        Assert.assertEquals(dataRow.cells().get(1).getName(),"name");
        Assert.assertEquals(dataRow.cells().get(2).getName(),"users");*/


        /****************************************************************************/


        /******************************** DE QEUÍ EXTRAERÉ EL DOBLE DE PRUEBA *****************************/

    }
}
