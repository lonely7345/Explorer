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
    When I click in the remove notebook button
    Then Notebook removed

  Scenario: Test Rename a Notebook
    Given: I press "add" notebook button
    And: Enter into notebook
    And: Click on notebook's name
    And: I change notebook's name
    When: I presss enter
    Then Notebook renamed

  Scenario: Test create 100 new Notebook
    Given I press "add" notebook button
    And: press add notebook button 100 times
    Then: I created 100 notebooks succesfully

  Scenario: Test remove 100 Notebook
    When I press "remove" notebook button
    And: press remove notebook button 100 times
    Then: I erased 100 notebooks succesfully

  Scenario: Select interpreter to work
    Given: I put "command" in the script window
    And: I select interpreter to work
    When: I press run button
    Then: It runs succesfully

  Scenario: Put multiples lines into editor
    When I put multiple "commands" into script editor
    And: Execute it
    Then: It works with the appropriate interpreter and execute all commands succesfully

  Scenario: Test 'Hide Editor' button
    Given: I put "command" in the script window
    When I press 'Hide editor button'
    Then: Editor hidded

  Scenario: Test Show editor button
    Given: I put "command" in the script window
    When I press 'Hide editor button'
    And: I press 'show editor button'
    Then: Editor showed

  #Test settings button
  #Button style and appearance of the paragraph
  Scenario: Test Change width column to 1 to 12
    When: I press settings button
    And: Select 1..12 widths
    Then: The Width of the script windows fit correctly

  Scenario: Test group 2 to 10 columns with different width
    When: I press settings button
    And: Select 2..10 widths
    Then: Windows fit correctly

  Scenario: Test group 2 to 12 columns with same width
    When: I press settings button
    And: Select 1..12 same widths in all windows
    Then: Windows fits correctly

  Scenario: Test show title of the paragraph
    When: I press settings button
    And: I press show title
    Then: title shows

  Scenario: Test hide title of the paragraph
    When: I press settings button
    And: I press hide title
    Then: title hides

  Scenario: Test edit title of the paragraph
    Given: I Enter into paragraph editor
    When: I press into settings button
    And:  I Click on show title option
    And: I click on the title
    And: I change title text
    And: I press enter
    Then Notebook's paragraph renamed succesfully

  Scenario: Test move up paragraph
    Given: I Enter into paragraph editor
    When: I press into settings button
    And:  I Click on "move up" option
    Then Notebook's paragraph moved up succesfully

  Scenario: Test move down paragraph
    Given: I Enter into paragraph editor
    When: I press into settings button
    And:  I Click on "move down" option
    Then Notebook's paragraph moved down succesfully

  Scenario: Test insert new paragraph
    Given: I Enter into paragraph editor
    When: I press into settings button
    And:  I Click on "Insert new" option
    Then I create a new Notebook's paragraph succesfully

  Scenario: Test remove paragraph
    Given: I Enter into paragraph editor
    When: I press into settings button
    And:  I Click on "remove" option
    Then I removed Notebook's paragraph succesfully

 #NoteBook options
  #JIRA - EXPLORER-143
  Scenario: Test show / hide the code
    Given: I Enter into paragraph editor
    And: Open >10 paragraphs windows
    And: I put a command in all windows
    When: I executed the commands
    And: I press "show / Hide the code" Notebook's option button
    Then It doesn't work

  Scenario: Test show / hide the output
    Given: I Enter into paragraph editor
    And: I put a command
    When: I executed the command
    And: I press "show / Hide the output" Notebook's option button
    Then It doesn't work

  Scenario: Test remove notebook
    Given: I Enter into Notebook
    When: I click on "remove the notebook"
    Then: notebook removed

    #JIRA - EXPLORER-134
  Scenario: Test export notebook
    Given: I Enter into Notebook
    When: I click on "export notebook" button
    And: I Press enter
    Then: notebook not saved

  Scenario: Test modify target directory to save notebook
    Given: I Enter into Notebook
    When: I click on "export notebook" button
    And: I change path of the Json file
    Then: notebook not saved

  Scenario: Test save current notebook status
    Given: I Enter into Notebook
    When: I click on "export notebook" button
    And: I change path of the Json file
    Then: notebook not saved

  Scenario: Test remove all paragraphs results
    Given: I Enter into Notebook
    When: I click on "removes all paragraph results" button
    Then: paragraph results removed

     #JIRA - EXPLORER-134 - Usability
  Scenario: Test select default/simple view
    Given: I Enter into Notebook
    When: I click on "type of view" select
    Then: View changed

  #General settings
  Scenario: Test edit config crossdata and save


  Scenario: Test edit config ingestion and save


  Scenario: Test edit config cassandra and save
    When: I click on "Stratio's" select
    When: I click on "settings" options
    Then: Config changed succesfully

  Scenario: Test logout
    Given: I Enter into Notebook
    When: I click on Statio's select
    And: I click logout option
    Then: user loged out


  Scenario: Test search field
    Given: I Enter into Notebook
    When: I click on search box
    And: I put "string" to find
    Then: it find succesfully


  Scenario: Test view help
    When: I click on Stratio's select
    And: I click on help
    Then: help is displayed

    #Test Shortcuts
  Scenario: Shift + Enter  Run the note
    When: I press Shift + Enter
    Then: I run the note

  Scenario: Ctrl + Spacebar  Syntax autocomplete helper
    When: I press Ctrl + Spacebar
    Then: It autocomplete succesfully

  Scenario: Ctrl + a  Select the whole line
    When: I press Ctrl + a
    Then: It select the whole line succesfully

  Scenario: Ctrl + x  Cut the line
    When: I press Ctrl + x
    Then: It cuts the line succesfully

  Scenario: Ctrl + v  Paste the line
    When: I press Ctrl + v
    Then: It pastes buffer succesfully

  Scenario: Up arrow Move cursor Up
    When: I press Up arrow
    Then: It Move cursor up succesfully

  Scenario: Down arrow Move cursor Down
    When: I press down arrow
    Then: It Move cursor down succesfully

    #JIRA - EXPLORER 140
  Scenario: Change global interpreter
    Given: I create 2 new script windows
    And: I change global interpreter
    Then: All script interpreter are changed.

    #JIRA - EXPLORER 141
  Scenario: Test button's tool tip
    Given: I move mouse on buttons
    Then: save button, open button, clock button haven't tool tip

    #button disabled?
  Scenario: Check empty fields markdown Interpreter
    Given empty 'editor' field
    And Select 'markdown' engine
    When Click run button
    Then return none









