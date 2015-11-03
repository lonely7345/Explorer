@ignore @manual
Feature: Explorer Shell Interaction
  In order to interact with the server linux Shell
  As a Stratio Explorer User
  I want to do send commands to the server using Stratio Explorer.


  Scenario: Check empty fields shell Engine
    Given empty 'editor' field
    And Select 'shell' engine
    When Click run button
    Then return none

