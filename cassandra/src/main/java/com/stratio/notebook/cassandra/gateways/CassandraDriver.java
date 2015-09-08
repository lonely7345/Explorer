package com.stratio.notebook.cassandra.gateways;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.SyntaxError;
import com.stratio.notebook.cassandra.constants.StringConstants;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.interpreter.InterpreterDriver;

import java.util.Properties;


//TODO : this class is not completed
public class CassandraDriver implements InterpreterDriver<DataTable>{


    private Session session;
    private int port ;
    private String host;

    public CassandraDriver(Properties properties){
        host = properties.getProperty(StringConstants.HOST);
        port = Integer.valueOf(properties.getProperty(StringConstants.PORT));
    }

    @Override public void connect() {
        try {
           if (session==null) {
               Cluster cluster = Cluster.builder().addContactPoint(host).withPort(port).build();
               session = cluster.connect();
           }
        }catch (NoHostAvailableException e){
            throw new ConnectionException(e.getMessage());
        }
    }


    @Override public DataTable executeCommand(String command) {
        try {
            return null;//session.execute(command);
        }catch (SyntaxError | InvalidQueryException e){
            throw new CassandraInterpreterException(e.getMessage());
        }
    }
}
