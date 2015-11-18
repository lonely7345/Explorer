@ignore @manual
Feature: Basic Explorer Operations
  In order to Know the Stratio Explorer basis
  As a Stratio Explorer User
  I want to do basic stuffs in Stratio Explorer

    #JIRA - EXPLORER-132
  Scenario: Check empty fields Ingestion interpreter
    Given empty 'editor' field
    And Select 'Ingestion' interpreter
    When Click run button
    Then infinite loop

  Scenario: Check start Ingestion
    Given a Configured Ingestion environment
    When I execute start command
    Then Ingestion process was started


  Scenario: Stop Ingestion
    Given a Configured Ingestion environment
    And a Started Ingestion Environment
    When I execute stop command
    Then Ingestion process was stopped

