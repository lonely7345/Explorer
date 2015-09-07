package com.stratio.notebook.cassandra.doubles;

import com.datastax.driver.core.ResultSet;
<<<<<<< HEAD
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver<DataTable>{
=======
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver{
>>>>>>> a


    private boolean isUpService;
    private boolean correctSyntax;
<<<<<<< HEAD
    private DataTable initialDataInStore;

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax,DataTable initialDataInStore){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
        this.initialDataInStore = initialDataInStore;
=======

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
>>>>>>> a
    }

    @Override public void connect() {
        if (!isUpService)
            throw new ConnectionException("exception");
    }


<<<<<<< HEAD
    @Override public DataTable executeCommand(String command) {
        if (!correctSyntax)
           throw new CassandraInterpreterException("exception");
        return initialDataInStore;
=======
    @Override public ResultSet executeCommand(String command) {
        if (!correctSyntax)
           throw new CassandraInterpreterException("exception");
        return null;
>>>>>>> a
    }
}
