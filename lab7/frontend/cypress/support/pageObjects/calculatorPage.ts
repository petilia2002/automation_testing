// cypress/support/pageObjects/calculatorPage.ts
class CalculatorPage {
  visit() {
    cy.visit('/');
  }

  // селекторы (используем атрибуты Angular)
  firstInput() {
    return cy.get('app-number-input[formcontrolname="firstNumber"] input');
  }
  secondInput() {
    return cy.get('app-number-input[formcontrolname="secondNumber"] input');
  }
  operationSelect() {
    return cy.get('select[formcontrolname="operationType"]');
  }
  firstNumberSystemSelect() {
    return cy.get('select[formcontrolname="firstNumberSystem"]');
  }
  secondNumberSystemSelect() {
    return cy.get('select[formcontrolname="secondNumberSystem"]');
  }
  precisionInput() {
    return cy.get('input[formcontrolname="precision"]');
  }
  calculateButton() {
    return cy.get('button[type="submit"]');
  }
  resultContainer() {
    return cy.get('.result');
  }
  resultMeta() {
    return cy.get('.meta');
  }

  // actions
  setFirst(value: string) {
    this.firstInput().clear().type(value);
  }
  setSecond(value: string) {
    this.secondInput().clear().type(value);
  }
  selectOperation(op: string) {
    this.operationSelect().select(op);
  }
  setFirstSystem(sys: string) {
    this.firstNumberSystemSelect().select(sys);
  }
  setSecondSystem(sys: string) {
    this.secondNumberSystemSelect().select(sys);
  }
  setPrecision(n: number) {
    this.precisionInput().clear().type(String(n));
  }
  clickCalculate() {
    this.calculateButton().click();
  }
}

export default new CalculatorPage();
