@ignore @manual
Feature: Basic Explorer Operations
  In order to Know the Stratio Explorer basis
  As a Stratio Explorer User
  I want to do basic stuffs in Stratio Explorer

  Background: Login user
    Given an user logged into Explorer

  Scenario: Test create new Notebook
    When I click in the add notebook button
    Then I get a new notebook with a random name

  Scenario: Test Delete a Notebook

  Scenario: Test Rename a Notebook

  Scenario: Test Organize a Notebooks


