@web
Feature: Explorer Login

  Background:

    Given I set web base url to '${EXPLORER_HOST}:${EXPLORER_PORT}'
    Given I browse to '/'
    Then '1' elements exists with 'id:username'
    And I type 'Stratio' on the element on index '0'
    Then '1' elements exists with 'id:password'
    And I type 'Stratio' on the element on index '0'
    Then I wait '1' seconds
    Then '1' elements exists with 'css:button[class="btn btn-info"]'
    Then I click on the element on index '0'
    Then '1' elements exists with 'id:new-note'
