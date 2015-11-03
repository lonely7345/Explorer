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

  Scenario: Test create 100 new Notebook

  Scenario: Test Delete 100 Notebook

  Scenario: Test Organize a Notebooks

  Scenario: Create a new notebook

  Scenario: Select engine to work

  Scenario: Put multiples lines into editor

  Scenario: Test 'Hide Editor' button

  Scenario: Test Show/Hide Editor button

  Scenario: Test Show/Hide Output button

  #Test settings button
  #Button style and appearance of the paragraph
  Scenario: Test Change width column to 1 to 12

  Scenario: Test group 2 to 10 columns with different width

  Scenario: Test group 2 to 12 columns with same width

  Scenario: Test show title of the paragraph

  Scenario: Test hide title of the paragraph

  Scenario: Test edit title of the paragraph

  Scenario: Test move up paragraph

  Scenario: Test move down paragraph

  Scenario: Test insert new paragraph

    ##button disabled?
  Scenario: Check empty fields markdown Engine
    Given empty 'editor' field
    And Select 'markdown' engine
    When Click run button
    Then return none









