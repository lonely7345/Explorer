@ignore @manual
Feature: Decision Integration
  In order to interact with Stratio Decision from Explorer
  As a Stratio Explorer User
  I want to do Decision's queries


  Background: Login user
    Given an user logged into Explorer
    And a new Notebook

    #JIRA - EXPLORER-132
  Scenario: Check empty fields decision
    Given: empty 'editor' field
    And: Select 'decision' interpreter
    When: Click run button
    Then: return decision interpreter error

    #JIRA - Explorer-151 (Decision Shell doesn't work)
  Scenario: Check lists all streams (and their associated actions) and queries with "list" command
    Given: I put "list" command into shell
    When: I press enter
    Then: I recibed "Error recognized: line 1:0: no viable alternative at input 'list' ?list" error

  Scenario: Check create a new stream with the given structure with "create" command
    Given: I put "create" command into shell
    When: I press enter
    Then:

  Scenario: Check remove the given stream from the engine with "drop" command
    Given: I put "drop" command into shell
    When: I press enter
    Then:

  Scenario: Check insert a new event in the given stream with "insert" command
    Given: I put "insert" command into shell
    When: I press enter
    Then:

  Scenario: Check add a query to the specified stream with "add query" command
    Given: I put "add query" command into shell
    When: I press enter
    Then:

  Scenario: Check remove the specified query from the given stream with "remove query" command
    Given: I put "remove query" command into shell
    When: I press enter
    Then:

  Scenario: Check list all streams querys in the engine with "columns" command
    Given: I put "columns" command into shell
    When: I press enter
    Then:

  Scenario: Check start indexing the stream in the defined Elasticsearch cluster, creating a new index with the stream name with "index start" command
    Given: I put "index start" command into shell
    When: I press enter
    Then:

  Scenario: Check stop indexing the stream in the defined Elasticsearch cluster with "index stop" command
    Given: I put "index stop" command into shell
    When: I press enter
    Then:

  Scenario: Check start sending the stream to the configured Kafka bus, creating a new topic with the stream name with "listen start"
    Given: I put "listen start" command into shell
    When: I press enter
    Then:

  Scenario: Check stop sending the stream to the configured Kafka bus with "listen stop"
    Given: I put "listen stop" command into shell
    When: I press enter
    Then:

  Scenario: Check start saving the stream in Cassandra, under the keyspace stratio_streaming and using a table with the stream name with "save cassandra start"
    Given: I put "save cassandra start" command into shell
    When: I press enter
    Then:

  Scenario: Check stop saving the stream in Cassandra with "save cassandra stop" command.
    Given: I put "save cassandra stop" command into shell
    When: I press enter
    Then:

  Scenario: Check start saving the given stream in MongoDB, using the database stratio_streaming and a collection with the stream name with "save mongo start" command.
    Given: I put "save mongo start" command into shell
    When: I press enter
    Then:

  Scenario: Check start indexing the given stream in Solr, creating and using a core with the stream name with "save solr start" command.
    Given: I put "save solr start" command into shell
    When: I press enter
    Then:

  Scenario: Check stop indexing the given stream in Solr with "save solr stop" command.
    Given: I put "save solr stop" command into shell
    When: I press enter
    Then:

