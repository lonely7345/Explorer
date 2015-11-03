@ignore @manual

Feature: Explorer-Cassandra Integration
  In order to Connect to Cassandra
  As a Stratio Explorer User
  I want to do some queries against Cassandra

  Background: Login user
    Given an user logged into Explorer
    And a dataset ingested in Cassandra
    And All required Crossdata Connectors started
    And a new Notebook

  Scenario: Check empty fields Cassandra Engine
    Given empty 'editor' field
    And Select 'Cassandra' engine
    When Click run button
    Then return Cassandra error

  Scenario: Test Create Keyspace

  Scenario: Test Create Table

  Scenario: Test Insert Into

  Scenario: Test Select *

  Scenario: Test Create Index

  Scenario: Test Create Secondary Index

  Scenario: Test Drop Table

  Scenario: Test Drop Index

  Scenario: Test Drop keyspace

  Scenario: Test Select with filters

  Scenario: Test Select with invalid filter

  Scenario: Test Select with no indexed filters

  Scenario: Test Select with filters in secondary index



