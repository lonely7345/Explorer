@ignore @manual

  #JIRA - EXPLORER-153
Feature: Explorer-SparkSQL Integration
  In order to Connect to SparkSQL and submit Queries
  As a Stratio Explorer User
  I want to do some request to SparkSQL

      #JIRA - EXPLORER-132
  Scenario: Check empty fields Spark-sql interpreter
    Given empty 'editor' field
    And Select 'Spark-sql' interpreter
    When Click run button
    Then infinite loop

    #Jira - EXPLORER-155
  Scenario: Check CREATE KEYSPACE
    Given a Songs Datasource in C*
    When I Execute "CREATE KEYSPACE test2 WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 3 }"
    Then I got the "Keyspace Created" message

    #Jira - EXPLORER-154
  Scenario: Check CreateTable Query
    Given a Songs Datasource in C*
    When I Execute "CREATE TEMPORARY TABLE songs USING org.apache.spark.sql.cassandra OPTIONS (table 'songs', keyspace 'songs', cluster 'Test Cluster', pushdown 'true', spark_cassandra_connection_host '127.0.0.1');"
    Then I got the "Table regstered successfully" message

    #Can't Test, CREATE TEMPORARY table required
  Scenario: Check Select Query
    Given a Songs Datasource in C*
    And the Songs table registerd
    When I Execute "select * from songs where artist_name = 'Bézu';"
    Then I got 8 results

    #Can't Test, CREATE TEMPORARY table required
  Scenario: Check Insert
    Given a Songs Datasource in
    And the Songs table register
    When I Execute "INSERT INTO songs.songs (track_id, title, song_id, release, artist_id, artist_mbid, artist_name, duration, artist_familiarity, artist_hotttnesss, year) VALUES('TRBGVWB12903CE31A6','The Darkness (Komor Kommando Mix)','SOOKYPR12AB018AD0C','Endzeit Bunkertracks - Act IV: The Alfa Matrix Selection','ARY02GH1187B99B223','050bd499-feba-4321-93d7-27b96a60d504','Zombie Girl',256.73098,0.639165318057,0.443395584874,2009);"
    Then A new file is registered