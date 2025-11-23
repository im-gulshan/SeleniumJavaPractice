Feature: Create Airline
  Scenario: Create an airline with valid data
    Given url 'https://gorest.co.in/public/v2/users/7583031'
    When method get
    Then status 200
    And print response
