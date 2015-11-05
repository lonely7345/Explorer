@ignore @manual

Feature: Explorer-Crossdata Integration
  In order to Connect to Stratio Crossdata and query different Databases
  As a Stratio Explorer User
  I want to do some queries against Stratio Crossdata


  Background: Login user
    Given an user logged into Explorer
    And a dataset ingested in Elasticsearch
    And All required Crossdata Connectors started
    And a new Notebook

  Scenario: Check empty fields crossdata Engine
    Given empty 'editor' field
    And Select 'crossdata' engine
    When Click run button
    Then return crossdata error

    ##OK
  Scenario: Test Basic Crossdata Commands
    When I execute a "DESCRIBE CONNECTORS;" in Crossdata
    Then I got the list with the connector states

    ##TODO
  Scenario: Test Modify Crossdata Settings

    ##OK
  Scenario: Test Crossdata Attach Cluster
    When I execute a "ATTACH CLUSTER elCluster ON DATASTORE elasticsearch WITH OPTIONS {'Hosts': '[172.31.14.170,172.31.12.106 ]', 'Native Ports': '[9300,9300]', 'Restful Ports': '[9200,9200]', 'Cluster Name':'Stratio ElasticSearch'};" in Crossdata
    Then I got "Cluster attached successfully"


  Scenario: Test Crossdata Attach Connector
    When I execute a "ATTACH CONNECTOR MongoConnector TO mongoDBCluster WITH OPTIONS {'default_limit' :'50'};"
    Then I got "Connected to cluster successfully"


  Scenario: Test Crossdata Create Catalog
    When I execute a "create catalog songs;"
    Then I got "CATALOG created successfully"

  Scenario: Test Crossdata Create Catalog with options
    When I execute a:
  CREATE CATALOG movies WITH {'settings' : '{"number_of_replicas" : "0","number_of_shards" : "1","analysis" : {"analyzer" : {"raw" :
  {"tokenizer" : "keyword"},"normalized" : {"filter" : ["lowercase"],"tokenizer" : "keyword"},"english" : {"filter" : ["lowercase",
  "english_stemmer"],"tokenizer" : "standard"}},"filter" : {"english_stemmer" : {"type" : "stemmer","language" : "english"}}}}'};
    Then I got "CATALOG created successfully"

  Scenario: Test Crossdata Register Table
    When I execute a
  REGISTER TABLE movies.movie ON CLUSTER elCluster (
  imdbID text("index": "not_analyzed" ) PRIMARY KEY,
  actors list<text>("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  awards text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  country list<text>("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  director text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  genre text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  imdbRating float,
  imdbVotes BIGINT,
  language text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  metascore int,
  plot text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  poster text("index": "not_analyzed"),
  rated text("index": "not_analyzed" ),
  released date("format": "yyyy-MM-dd'T'HH:mm:ssZ", "locale": "us"),
  runtime text("index": "not_analyzed"),
  title text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  type text("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  writers list<text>("analyzer":"english", "analyzer":"lowecase", "analyzer":"raw"),
  year int
  );
    Then I got "TABLE registered successfully"

  Scenario: Test Crossdata Create Table command
    When I execute a
  CREATE TABLE testel.movie ON CLUSTER elCluster (id text("index":"not_analyzed") PRIMARY KEY, title text("analyzer":"english",
  "analyzer":"normalized", "analyzer":"raw"), released boolean);
    Then I got "TABLE created successfully"


      ##OK
  Scenario: Test Basic Crossdata Commands without semicolon
    When I execute a "DESCRIBE CONNECTORS" in Crossdata
    Then I got the list with the connector states

  Scenario: Test Basic Crossdata Commands with errors
    When I execute a "DESCRIBE CONNES;" in Crossdata
    Then I got "Unknown command"

    ##OK
  Scenario: Test Crossdata Attach Cluster With Bad Options
    When I execute a "ATTACH CLUSTER badCluster ON DATASTORE elasticsearch WITH OPTIONS {};" in Crossdata
    Then I got "Some required properties are missing"

  Scenario: Test Crossdata Attach Cluster With Bad Options
    When I execute a "ATTACH CLUSTER badCluster ON DATASTORE elasticsearch WITH OPTIONS {""};" in Crossdata
    Then I got "Parser exception"

    ##OK
  Scenario: Test Crossdata Attach Connector that does not exist
    When I execute a "ATTACH CONNECTOR MANGAConnector TO mongoDBCluster WITH OPTIONS {'default_limit' :'50'};"
    Then I got "[connector.MANGAConnector]  doesn't exist yet"

    ##OK
  Scenario: Test Crossdata Attach Connector in a cluster that does not exist
    When I execute a "ATTACH CONNECTOR MongoConnector TO mangaCluster WITH OPTIONS {'default_limit' :'50'};"
    Then I got "[cluster.mangaCluster]  doesn't exist yet"

    ##OK
  Scenario: Test Crossdata Attach Connector with bad syntax's
    When I execute a "ATTACH CONNECTOR MongoConnector TO mongoDBCluster WITH OPTIONS {'default_limit' :'50'};"
    Then I got "Parser exception"

    ##OK
  Scenario: Test Crossdata Attach Connector with no options
    When I execute a "ATTACH CONNECTOR CassandraConnector TO cassandraCluster WITH OPTIONS {};"
    Then I got "Some required properties are missing"

    ##OK
  Scenario: Test Crossdata Attach Connector already attached
    When I execute a "ATTACH CONNECTOR MongoConnector TO mongoDBCluster WITH OPTIONS {'default_limit' :'50'};"
    Then I got "ERROR: Couldn't connect to cluster: The connection [mongoDBCluster] already exists"

        ##OK
  Scenario: Test Crossdata Attach Connector Bad Syntax
    When I execute a "ATTACH adasgsag CONNECTOR CassandraConnector TO cassandraCluster WITH OPTIONS {};"
    Then I got "Parser exception"


  Scenario: Test Crossdata INSERT command with bad syntax
    Given a "testtel" Catalog
    And the cluster "elCluster" attached to "elasticsearch" connector
    And  "movie" table created
    When I execute a "INSERT INTO testtel.movie (id, title, released) VALUES ('aaaa', true);"
    Then I got "Parser exception:"


  Scenario: Test Crossdata Create Table with list column
    Given a "explorer" Catalog
    And the cluster "mongoDBCluster" attached to "mongodb" connector
    When I execute a
  CREATE TABLE explorer.listCollection ON CLUSTER mongoDBCluster (id text PRIMARY KEY, name text, listColumn list<int>);
    Then I got "TABLE created successfully"

  Scenario: Test Crossdata INSERT command with list Type
    Given a "explorer" Catalog
    And the cluster "mongoDBCluster" attached to "mongodb" connector
    And a table "explorer.listCollection" created
    When I execute a "INSERT INTO explorer.listCollection (id, name, listColumn) VALUES ('123', 'luis', [1,2,3]);"
    Then I got "Parser exception:"

    //DOES NOT WORK...
  REGISTER TABLE songs.songs ON CLUSTER cassandraCluster (track_id text PRIMARY KEY, title text, song_id text, release text, artist_id text, artist_mbid text, artist_name text, duration double, artist_familiarity double, artist_hotttnesss double, year int);

  Scenario: Test Crossdata Select * Without filters

  Scenario: Test Crossdata Select * With simple filters

  Scenario: Test Crossdata Select * With multiple filters

  Scenario: Test Crossdata Select * With Order By

  Scenario: Test Crossdata Select * With Limit

  Scenario: Test Crossdata Select With projections

  Scenario: Test Crossdata Select Into

  Scenario: Test Crossdata Select With Inner Join

  Scenario: Test Crossdata Select GROUP BY

  Scenario: Test Crossdata DELETE FROM

  Scenario: Test Crossdata CREATE INDEX

  Scenario: Test Crossdata CREATE Secondary Index in C*

  Scenario: Test Crossdata Update command

  Scenario: Test Crossdata DROP Index

  Scenario: Test Crossdata DROP Table

  Scenario: Test Crossdata DROP Catalog

  Scenario: Test Crossdata Detach Cluster

  Scenario: Test Crossdata Select GroupBy And display a graph