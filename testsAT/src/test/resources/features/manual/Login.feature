

Scenario: Test Login
Given a Running Explorer server
And an the user "Stratio" and password "Stratio"
When I go to "http://54.84.170.202:8084/" and fill the login form
Then I get into Explorer app

Scenario: Test Wrong User
Given a Running Explorer server
And an the user "BadUser" and password "Stratio"
When I go to "http://54.84.170.202:8084/" and fill the login form
Then I get an http "forbidden" error

Scenario: Test Wrong Password
Given a Running Explorer server
And an the user "Stratio" and password "BadPassword"
When I go to "http://54.84.170.202:8084/" and fill the login form
Then I get an http "forbidden" error
