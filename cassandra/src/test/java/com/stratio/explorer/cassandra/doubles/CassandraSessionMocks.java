package com.stratio.explorer.cassandra.doubles;

import com.datastax.driver.core.*;
import com.stratio.explorer.cassandra.gateways.CassandraSession;
import javafx.beans.binding.When;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Mock Metadadata
 */
public class CassandraSessionMocks {


   public   CassandraSession mockDescribeCluster(String clusterName,String partitioner){

       Metadata metaData = mock(Metadata.class);
       when(metaData.getClusterName()).thenReturn(clusterName);
       when(metaData.getPartitioner()).thenReturn(partitioner);
       Cluster cluster = mock(Cluster.class);
       when(cluster.getMetadata()).thenReturn(metaData);

       Session session = mock(Session.class);
       when(session.getCluster()).thenReturn(cluster);

       CassandraSession cassandra = mock(CassandraSession.class);
       when(cassandra.getConnector()).thenReturn(session);

       return cassandra;
    }



    public CassandraSession mockDescribeKeySpace(String nameTable){


        KeyspaceMetadata keySpaceMetaData = mock(KeyspaceMetadata.class);
        when(keySpaceMetaData.getName()).thenReturn(nameTable);

        Metadata metaData = mock(Metadata.class);
        when(metaData.getKeyspaces()).thenReturn(buildList(keySpaceMetaData));

        Cluster cluster = mock(Cluster.class);
        when(cluster.getMetadata()).thenReturn(metaData);

        Session session = mock(Session.class);
        when(session.getCluster()).thenReturn(cluster);

        CassandraSession cassandra = mock(CassandraSession.class);
        when(cassandra.getConnector()).thenReturn(session);

        return cassandra;

    }

    private List<KeyspaceMetadata> buildList(KeyspaceMetadata... metaDatas){

       List<KeyspaceMetadata> result = new ArrayList<>();
        for (KeyspaceMetadata metadata:metaDatas){
            result.add(metadata);
        }

       return result;
    }













}
