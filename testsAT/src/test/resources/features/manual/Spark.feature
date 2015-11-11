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


  Scenario: Test simple Spark Script

  Scenario: Test Spark Script with RDDs

  Scenario: Test Spark Streaming capabilities

  Scenario: Test Spark MLib capabilities

    Scenario: Load HDFS file
    val counts = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_ + _)

  Scenario: Test create simple array
    Given: Create simple array with "Array(1,2,3,4,5)" command
    Then: Array created succesfully

  Scenario: Test parallelize data
    Given: Create parallelize with "sc.parallelize(data)" command
    Then: Transform created succesfully

  Scenario: Test collect data
    Given: Ckeck Collect data with "collect" command
    Then: Action works succesfully

  Scenario: Test mapping pairs
    Given: Ckeck mapping pairs of data with "map" transform
    Then: Transform created succesfully

  Scenario: Test take command
    Given: Ckeck "take" action
    Then: Action works succesfully

  Scenario: Test reduce command
    Given: Ckeck "reduce" transform
    Then: Transform created succesfully

  Scenario: Test take command
    Given: Ckeck "reduceByKey" action
    Then: Transform created succesfully