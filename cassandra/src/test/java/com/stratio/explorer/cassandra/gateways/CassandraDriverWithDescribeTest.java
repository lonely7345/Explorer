package com.stratio.explorer.cassandra.gateways;


import com.stratio.explorer.cassandra.doubles.CassandraSessionMocks;
import com.stratio.explorer.cassandra.gateways.executors.DescribeClusterExecutor;
import com.stratio.explorer.cassandra.gateways.executors.DescribeKeyspaceExecutor;
import com.stratio.explorer.cassandra.models.CellData;
import com.stratio.explorer.cassandra.models.RowData;
import com.stratio.explorer.cassandra.models.Table;
import org.junit.Test;

import javax.swing.text.TableView;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class CassandraDriverWithDescribeTest {


    // DESCRIBE CLUSTER; (Inlude Table) (done)
    // DESCRIBE KEYSPACES; **
    // DESCRIBE KEYSPACE PortfolioDemo; **
    // DESCRIBE TABLES; **
    // DESCRIBE TABLE Stocks;

    @Test
    public void whenDescribeCluster(){
        String clusterName = "clusetrName";
        String partitioner = "partitioner";
        CassandraDriverWithDescribe describe = new CassandraDriverWithDescribe(new CassandraSessionMocks().mockDescribeCluster(clusterName,partitioner));
        Table table = describe.execute("DESCRIBE CLUSTER");
        assertThat(table.header(), is(buildList(DescribeClusterExecutor.CT_CLUSTER, DescribeClusterExecutor.CT_PARTIRIONER)));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(clusterName));
        assertThat(table.rows().get(0).cells().get(1).getValue().toString(),is(partitioner));
    }

    @Test
    public void whenDescribeKeySpaces(){
        String nameKeySpace ="unicquename";
        CassandraDriverWithDescribe describe = new CassandraDriverWithDescribe(new CassandraSessionMocks().mockDescribeKeySpace(nameKeySpace));
        Table table = describe.execute("DESCRIBE KEYSPACES");
        assertThat(table.header(), is(buildList(DescribeKeyspaceExecutor.CT_KEYSPACES)));
        assertThat(table.rows().get(0).cells().get(0).getValue().toString(),is(nameKeySpace));

    }


    private Table buildTableCluster( String... values){
        List<CellData> cells = new ArrayList<>();
        for(String value:values) {
            cells.add(new CellData(value));
        }
        RowData rowData = new RowData(cells);
        List<RowData> data = new ArrayList<>();
        data.add(rowData);
        Table table = new Table(buildList(DescribeClusterExecutor.CT_CLUSTER,DescribeClusterExecutor.CT_PARTIRIONER),data);
        return table;

    }


    private List<String> buildList(String... cadResults){
       List<String> result = new ArrayList<>();
       for (String cad:cadResults){
           result.add(cad);
       }
       return result;
    }
}
