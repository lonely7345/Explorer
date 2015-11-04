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

  Scenario: Check empty fields crossdata interpreter
    Given empty 'editor' field
    And Select 'crossdata' interpreter
    When Click run button
    Then return crossdata error

  Scenario: Test Basic Crossdata Commands
    When I execute a "DESCRIBE CONNECTORS" in Crossdata
    Then I got the list with the connector states

  Scenario: Test Crossdata Attach Cluster

  Scenario: Test Crossdata Attach Connector

  Scenario: Test Crossdata Create Catalog

  Scenario: Test Crossdata Create Catalog with options

  Scenario: Test Crossdata Register Table

  Scenario: Test Crossdata Create Table command

  Scenario: Test Crossdata INSERT command

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