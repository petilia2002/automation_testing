Feature: Calculator page E2E tests
  As a user
  I want calculator UI to work correctly
  So that I can perform arithmetic operations

  Background:
    Given I open the calculator page

  Scenario: Page elements exist
    Then I should see first input
    And I should see second input
    And I should see operation dropdown
    And I should see calculate button

  Scenario Outline: Arithmetic operations work (mocked)
    Given I mock calculate response with fixture "<fixture>"
    When I set first number to "<first>"
    And I set second number to "<second>"
    And I select operation "<op>"
    And I click calculate
    Then the result should be "<expected>"

    Examples:
      | fixture      | first | second | op     | expected                  |
      | add.json     | 3     | 4      | ADD    | 7                        |
      | subtract.json| 5     | 30     | SUBTRACT | -25                    |
      | multiply.json| 5     | 10     | MULTIPLY | 50                     |
      | divide.json  | 5     | 30     | DIVIDE | 0.16666666666666666667   |

  Scenario: Inputs accept only digits for decimal system
    When I set first number to "12"
    And I set second number to "a2b3"
    Then first input should contain "12"
    And second input should contain "23"   # letters should be ignored

  Scenario: For division, second input cannot accept "0"
    Given I select operation "DIVIDE"
    When I attempt to type "0" into second input
    Then second input should not contain "0"

  Scenario: Hexadecimal input accepts letters
    Given I set first number system to "HEXADECIMAL"
    When I type "1aF" into first input
    Then first input should contain "1aF"

  Scenario: Result color depends on sign (mocked)
    Given I mock calculate response with fixture "subtract.json"
    When I perform calculation with first "5" second "30" op "SUBTRACT"
    Then result should have color "red"
