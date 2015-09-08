package com.stratio.notebook.cassandra.doubles;

import com.datastax.driver.core.ResultSet;
<<<<<<< HEAD
<<<<<<< HEAD
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.interpreter.InterpreterDriver;


public class DoubleCassandraDriver implements InterpreterDriver<DataTable>{
=======
import com.datastax.driver.core.exceptions.InvalidQueryException;
=======
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .
import com.stratio.notebook.cassandra.exceptions.CassandraInterpreterException;
import com.stratio.notebook.cassandra.exceptions.ConnectionException;
import com.stratio.notebook.cassandra.models.DataTable;
import com.stratio.notebook.interpreter.InterpreterDriver;


<<<<<<< HEAD
public class DoubleCassandraDriver implements InterpreterDriver{
>>>>>>> a
=======
public class DoubleCassandraDriver implements InterpreterDriver<DataTable>{
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .


    private boolean isUpService;
    private boolean correctSyntax;
<<<<<<< HEAD
<<<<<<< HEAD
    private DataTable initialDataInStore;

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax,DataTable initialDataInStore){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
        this.initialDataInStore = initialDataInStore;
=======
=======
    private DataTable initialDataInStore;
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .

    public DoubleCassandraDriver(boolean isUpService,boolean correctSyntax,DataTable initialDataInStore){
        this.isUpService = isUpService;
        this.correctSyntax = correctSyntax;
<<<<<<< HEAD
>>>>>>> a
=======
        this.initialDataInStore = initialDataInStore;
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .
    }

    @Override public void connect() {
        if (!isUpService)
            throw new ConnectionException("exception");
    }


<<<<<<< HEAD
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
=======
    @Override public DataTable executeCommand(String command) {
        if (!correctSyntax)
           throw new CassandraInterpreterException("exception");
        return initialDataInStore;
>>>>>>> build models and dto to transform cassandra table in JSON object , inital architecre to cassandra interpreter .
    }
}
