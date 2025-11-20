// cypress/e2e/step_definitions/calculator.steps.ts
import {
  Given,
  When,
  Then,
} from '@badeball/cypress-cucumber-preprocessor/steps';
import CalculatorPage from '../../../../support/pageObjects/calculatorPage';

Given('I open the calculator page', () => {
  CalculatorPage.visit();
});

// mock API
Given(
  'I mock calculate response with fixture {string}',
  (fixtureName: string) => {
    cy.intercept('POST', '**/api/calculator/calculate', {
      fixture: `/${fixtureName}`,
    }).as('calculate');
  }
);

// helpers
When('I set first number to {string}', (val: string) => {
  CalculatorPage.setFirst(val);
});

When('I set second number to {string}', (val: string) => {
  CalculatorPage.setSecond(val);
});

When('I select operation {string}', (op: string) => {
  CalculatorPage.selectOperation(op);
});

When('I click calculate', () => {
  CalculatorPage.clickCalculate();
  cy.wait('@calculate');
});

Then('the result should be {string}', (expected: string) => {
  CalculatorPage.resultContainer().should('contain', expected);
});

// existence checks
Then('I should see first input', () => {
  CalculatorPage.firstInput().should('exist').and('be.visible');
});
Then('I should see second input', () => {
  CalculatorPage.secondInput().should('exist').and('be.visible');
});
Then('I should see operation dropdown', () => {
  CalculatorPage.operationSelect().should('exist').and('be.visible');
});
Then('I should see calculate button', () => {
  CalculatorPage.calculateButton().should('exist').and('be.visible');
});

// digits-only test (decimal)
Then('first input should contain {string}', (expected: string) => {
  CalculatorPage.firstInput().should('have.value', expected);
});
Then('second input should contain {string}', (expected: string) => {
  CalculatorPage.secondInput().should('have.value', expected);
});

// For division forbid zero
When('I attempt to type {string} into second input', (text: string) => {
  CalculatorPage.secondInput().clear().type(text);
});
Then('second input should not contain {string}', (forbidden: string) => {
  CalculatorPage.secondInput().should('not.have.value', forbidden);
});

// hex letters allowed
Given('I set first number system to {string}', (sys: string) => {
  CalculatorPage.setFirstSystem(sys);
});
When('I type {string} into first input', (text: string) => {
  CalculatorPage.firstInput().clear().type(text);
});
Then('first input should contain {string}', (expected: string) => {
  CalculatorPage.firstInput().should('have.value', expected);
});

// color check
When(
  'I perform calculation with first {string} second {string} op {string}',
  (f: string, s: string, op: string) => {
    CalculatorPage.setFirst(f);
    CalculatorPage.setSecond(s);
    CalculatorPage.selectOperation(op);
    CalculatorPage.clickCalculate();
    cy.wait('@calculate');
  }
);

Then('result should have color {string}', (colorName: string) => {
  const colorMap: Record<string, string> = {
    red: 'rgb(255, 0, 0)',
    black: 'rgb(0, 0, 0)',
    green: 'rgb(0, 128, 0)',
  };
  const expected = colorMap[colorName] ?? colorName;
  CalculatorPage.resultContainer().should(($el) => {
    const computed = getComputedStyle($el[0]).color;
    // сравниваем по подстроке "r, g, b"
    expect(computed).to.contain(
      expected.split('(')[1]?.replace(')', '') || expected
    );
  });
});
