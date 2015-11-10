@ignore @manual

Feature: Explorer-Cassandra Integration
  In order to Connect to Cassandra
  As a Stratio Explorer User
  I want to do some queries against Cassandra

  Background: Login user
    Given an user logged into Explorer
    And a dataset ingested in Cassandra
    And All required Crossdata Connectors started
    Then: a new Notebook

  Scenario: Check empty fields Cassandra interpreter
    Given empty 'editor' field
    And Select 'Cassandra' interpreter
    When Click run button
    Then return Cassandra error

  Scenario: Test Create Keyspace
    When I try to create keySpace with "CREATE KEYSPACE prueba WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'};" command;
    Then Keyspace created

  Scenario: Test 'use' command
    When I try to change keyspace to "prueba" with "use prueba" command
    Then Keyspace changed

  Scenario: Test Create Table
    When I try to create table with "CREATE TABLE" command
    Then table created

    #JIRA - EXPLORER-133
  Scenario: Test "describe" command
    When I try to describe table with "describe songs" command
    Then Interpreter return: Query to execute in cassandra database is not correct

  Scenario: Test Insert Into
    When I try to insert reg into table with "insert into" command
    Then: Reg inserted

  Scenario: Test Select *
    When I try to select regs with "select *" command
    Then: Regs show inserted

  Scenario: Test Create Index
    When: I try to create index with "create index" command
    Then: index created

  Scenario: Test Drop Table
    When: I try to drop table with "drop table" command
    Then: table dropped

  Scenario: Test Drop Index
    When: I try to drop index with "drop index" command
    Then: index dropped

  Scenario: Test Drop keyspace
    When: I try to drop keyspace with "drop keyspace" command
    Then: table dropped

  Scenario: Test limit clause
    When: I try to select with limit clause "10"
    Then: It shows results succesfully

  Scenario: Test Select with filters
    When: I try to select with filter in column "year"
    And I Create index in year column
    Then: It shows results succesfully

  Scenario: Test Select with invalid filter
    When: I try to select with filter in column "year"
    And I Create index in year column
    Then: It shows results succesfully

  Scenario: Test Select with no indexed filters
    When: I try to "select *" with no indexed column filters
    And I drop any column index
    And The query is filtered by no indexed column / no partition column
    Then: It return query incorrect

  Scenario: Test alter keyspace command
    When: I try to alter a existing keyspace with "alter keyspace" command
    Then: KeySpace altered succesfully

  Scenario: Test Create Secondary Index
    Given: I create "keyspace"
    And: I create table "users"
    And: I try to create a secondary lucene index with "create custom index"
    When: Press enter
    Then: Index created succesfully

    #JIRA - EXPLORER 147
  Scenario: Test Create a existing keyspace
    When: I try to create a existing keyspace
    Then: infinite loop

  Scenario: Test Select with filters in secondary index
    When:
    Then:

    #EXPLORER-138
  Scenario: Test Execute "script" with various command
    When: I execute script with various command
    Then: Interpreter returns: Query to execute in cassandra database is not correct


