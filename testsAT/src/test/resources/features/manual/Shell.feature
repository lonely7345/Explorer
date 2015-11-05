@ignore @manual
Feature: Explorer Shell Interaction
  In order to interact with the server linux Shell
  As a Stratio Explorer User
  I want to send commands to the server using Stratio Explorer.

    #JIRA - EXPLORER-132
  Scenario: Check empty fields shell interpreter
    Given empty 'editor' field
    And Select 'shell' interpreter
    When Click run button
    Then return none

  Scenario: Test 'cd' command
    When I change $HOME directory with 'cd' command
    Then I change directory successfully

  Scenario: Test 'pwd' command
    When I want to see current directory with 'pwd' command
    Then It show current directory successfully

    #JIRA - EXPLORER-131
  Scenario: Test 'ls | grep s*' command
    When I want to show the files starts with 's*'
    Then I get Process exited with an error: 2 (Exit value: 2)

    #JIRA - EXPLORER-131
  Scenario: Test 'who i am' command
    When I want to show the current user
    Then I get no response

    #JIRA - EXPLORER-131
  Scenario: Test 'who' command
    When I want to show users logged
    Then I get Error recognized: line 1:0: no viable alternative at input 'who'

    #Not issue
  Scenario: Test create file in / folder
  When I try to create file in / folder
  Then I get Process exited with an error: 1 (Exit value: 1)

  Scenario: Test create file in /opt/sds folder
    Given "cd /opt/sds folder"
    When I try to create file with "echo "hola" > prueba.txt" command
    Then File created successfully

  Scenario: Test 'cat' command
    When I try to show the file contains
    Then I get the file contains

  Scenario: Test 'cat' command with various arguments
    Given create "file_1.txt" file
    And   create "file_2.txt" file
    When I try to concatenate various files with "cat file_1.txt file_2.txt > file1_2.txt" command
    Then I get the file concatenated

  Scenario: Test 'cp' command
    When I try to copy file with 'cp prueba.txt copia_prueba.txt' command
    Then File copied

  Scenario: Test 'rm' command
    When I try to remove file with "rm copia_prueba.txt" command
    Then File removed

  Scenario: Test 'rm' command with various files
    Given create temporal folder "$HOME/temp"
    And create "file_1.txt" file
    And   create "file_2.txt" file
    When I try to remove files with 'rm *' command
    Then Files are removed

  Scenario: Test 'mv' command
    When I try to move file with 'mv prueba.txt copia_prueba.txt' command
    Then File moved

  Scenario: Test 'mount' command
    When I try to show mounted File systems with "mount" command
    Then I got the list of mounted File Systems

  Scenario: Test 'df' command
    When I try to see free space of the File Systems with "df -h" command
    Then I got the list free space of the File Systems

  Scenario: Test 'du' command
    When I try estimate file space usage "du ." command
    Then I got the estimate usage

  Scenario: Test 'ls' with pipe command
    When I want to count number of files of the folder with "ls | wc -l" command
    Then I get number of files

  Scenario: Test 'rm' with recursive flag
    Given: Create "temp" folder
    And:  Create "temporal file" into temp folder
    When I want to remove the temporary folder that contains temporal file with "rm -r temp" command
    Then folder removed recursively

    #JIRA - EXPLORER-130
  Scenario: Test 'vi' editor
    When I want to edit file with vi editor "vi foo.txt" command
    Then I get infinite loop

    #JIRA - EXPLORER-130
  Scenario: Test 'nano' editor
    When I want to edit file with vi editor "nano foo.txt" command
    Then I get infinite loop

    #JIRA - EXPLORER-130
  Scenario: Test 'top' command
    When I want to show number and proccesor usage
    Then I get infinite loop

  Scenario: Test 'touch' command
    When I want to create file with "touch" command
    Then File created

  Scenario: Test 'ln' with flag -s
    When I want to create symbolic link with "ln -s foo.txt foo_link.txt" command
    Then Symbolic link created

  Scenario: Test 'ln' hard link
    When I want to create link with "ln foo.txt foo_hardLink.txt" command
    Then Link created