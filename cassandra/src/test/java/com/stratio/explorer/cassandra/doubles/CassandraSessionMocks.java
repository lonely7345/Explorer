package com.stratio.explorer.cassandra.doubles;

import com.datastax.driver.core.*;
import com.stratio.explorer.cassandra.gateways.CassandraSession;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Mock Metadadata
 */
public class CassandraSessionMocks {


   public   Session mockDescribeCluster(String clusterName,String partitioner){

       Metadata metaData = mock(Metadata.class);
       when(metaData.getClusterName()).thenReturn(clusterName);
       when(metaData.getPartitioner()).thenReturn(partitioner);
       Cluster cluster = mock(Cluster.class);
       when(cluster.getMetadata()).thenReturn(metaData);

       Session session = mock(Session.class);
       when(session.getCluster()).thenReturn(cluster);
       return session;
    }



    public Session mockDescribeKeySpaces(String nameTable){

        KeyspaceMetadata keySpaceMetaData = mock(KeyspaceMetadata.class);
        when(keySpaceMetaData.getName()).thenReturn(nameTable);

        Metadata metaData = mock(Metadata.class);
        when(metaData.getKeyspaces()).thenReturn(buildList(keySpaceMetaData));

        Cluster cluster = mock(Cluster.class);
        when(cluster.getMetadata()).thenReturn(metaData);

        Session session = mock(Session.class);
        when(session.getCluster()).thenReturn(cluster);

        return session;
    }


    public Session mockDescribe_keySpace_any(String keySpaceName,String createDemoScript,String creationFirstTable,String creationSecond){

        KeyspaceMetadata metaDataDemo = mock(KeyspaceMetadata.class);
        when(metaDataDemo.toString()).thenReturn(createDemoScript);
        doReturn(buildCollection(creationFirstTable, creationSecond)).when(metaDataDemo).getTables();

        Metadata metaData = mock(Metadata.class);
        when(metaData.getKeyspace(keySpaceName)).thenReturn(metaDataDemo);


        Cluster cluster = mock(Cluster.class);
        when(cluster.getMetadata()).thenReturn(metaData);

        Session session = mock(Session.class);
        when(session.getCluster()).thenReturn(cluster);

        return session;
    }


    private Collection<TableMetadata> buildCollection(String creationFirstTable,String creationSecond){
        Collection<TableMetadata> collection = new ArrayList<TableMetadata>();

        TableMetadata firstTable = mock(TableMetadata.class);
        when(firstTable.toString()).thenReturn(creationFirstTable);
        collection.add(firstTable);

        TableMetadata secondTable = mock(TableMetadata.class);
        when(secondTable.toString()).thenReturn(creationSecond);
        collection.add(secondTable);


        return collection;
    }


    private List<KeyspaceMetadata> buildList(KeyspaceMetadata... metaDatas){

       List<KeyspaceMetadata> result = new ArrayList<>();
        for (KeyspaceMetadata metadata:metaDatas){
            result.add(metadata);
        }

       return result;
    }
}
