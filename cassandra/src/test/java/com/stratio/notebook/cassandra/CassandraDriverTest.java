package com.stratio.notebook.cassandra;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Token;
import com.stratio.notebook.cassandra.constants.MessageConstants;
import com.stratio.notebook.cassandra.drivers.CassandraDriver;
import org.junit.Test;

import java.util.List;
import java.util.Properties;



public class CassandraDriverTest {

    private CassandraDriver driver;

    @Test public void whenClusterIsShutDown(){
        Properties properties = new Properties();
        properties.setProperty(MessageConstants.HOST,"127.0.0.1");
        properties.setProperty(MessageConstants.PORT,"9042");

        driver = new CassandraDriver(properties);
        driver.connect();
        driver.executeCommand("DROP TABLE demo.department;");
        driver.executeCommand("DROP TYPE demo.user;");
        driver.executeCommand("CREATE TYPE demo.user (name text,phones list<text>);");
        driver.executeCommand("CREATE TABLE demo.department (id int PRIMARY KEY ,name text , users list<frozen<demo.user>>);");
        driver.executeCommand("INSERT INTO demo.department  (id,name,users) values (1,'department_1',[{name:'fisrt_name',phones:['1234','1234']}]);");

      /******************************** DE QEUÍ EXTRAERÉ EL DOBLE DE PRUEBA *****************************/
        ResultSet rs = (ResultSet)driver.executeCommand("SELECT * FROM demo.department ;");

        ColumnDefinitions definitions =rs.getColumnDefinitions();
        List<ColumnDefinitions.Definition> defin = definitions.asList();
        List<Row> rows = rs.all();
        Row row =  rows.get(0);
        row.getObject(0);

        Token token =row.getToken("name");
        Object c =token.getValue();
        int a=0;
    }
}
