Feature: A client wants to make a Rest Request
@ignore
  Scenario: A client passes valid request to the generic rest template
    Given valid parameters
    When the request is made
    Then a json array string is return with size greater than 0
@ignore
  Scenario: A client passes invalid request to the generic rest template
    Given an invalid parameters
    When the request is made
    Then an exception is thrown
