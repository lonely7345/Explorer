@ignore @manual
Feature: Basic Explorer Operations
  In order to Know the Stratio Explorer basis
  As a Stratio Explorer User
  I want to do basic stuffs in Stratio Explorer


  Scenario: Check empty fields Ingestion Engine
    Given empty 'editor' field
    And Select 'Ingestion' engine
    When Click run button
    Then infinite loop
