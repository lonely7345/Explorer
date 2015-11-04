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
    Given empty 'editor' field
    And Select 'decision' interpreter
    When Click run button
    Then return decision interpreter error
