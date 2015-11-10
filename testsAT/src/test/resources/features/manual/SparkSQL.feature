@ignore @manual

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


  Scenario: Check CreateTable Query
    Given a Songs Datasource in C*
    When I Execute "CREATE TEMPORARY TABLE songs USING org.apache.spark.sql.cassandra OPTIONS (table 'songs', keyspace 'songs', cluster 'Test Cluster', pushdown 'true', spark_cassandra_connection_host '127.0.0.1');"
    Then I got the "Table regstered successfully" message

  Scenario: Check Select Query
    Given a Songs Datasource in C*
    And the Songs table registerd
    When I Execute "select * from songs where artist_name = 'Bézu';"
    Then I got 8 results

  Scenario: Check Select With Join
    Given a Songs Datasource in C*
    And the Songs table registerd
    When I Execute "select songs.artist_name, songs.release, artist_term.term from songs INNER JOIN artist_term ON (songs.artist_id = artist_term.artist_id);"
    Then I got results