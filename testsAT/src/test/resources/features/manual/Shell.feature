@ignore @manual
Feature: Explorer Shell Interaction
  In order to interact with the server linux Shell
  As a Stratio Explorer User
  I want to send commands to the server using Stratio Explorer.


  Scenario: Check empty fields shell Engine
    Given empty 'editor' field
    And Select 'shell' engine
    When Click run button
    Then return none


  Scenario: Test 'cd' command

  Scenario: Test 'pwd' command

  Scenario: Test 'ls | grep s*' command

  Scenario: Test 'cd' command

  Scenario: Test 'cd' command

  Scenario: Test 'cd' command

  Scenario: Test 'cd' command
