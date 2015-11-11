@ignore @manual

Feature: Explorer-Spark Integration
  In order to Connect to Spark and submit spark jobs
  As a Stratio Explorer User
  I want to do some request to Spark

    #JIRA - EXPLORER-132
  Scenario: Check empty fields Spark interpreter
    Given empty 'editor' field
    And Select 'Spark' interpreter
    When Click run button
    Then infinite loop

  Scenario: Test simple Spark Command

  Scenario: Test simple Spark Script

  Scenario: Test Spark Script with RDDs

  Scenario: Test Spark Script with RDDs

  Scenario: Test Spark Streaming capabilities

  Scenario: Test Spark MLib capabilities

    #JIRA - EXPLORER-157
  Scenario: Load HDFS file
    Given a file "songs.txt" stored in HDFS
    And a val named textFile in the context with the data of "songs.txt"
    When I execute "val counts = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_ + _).collect"
    Then I got the file data



