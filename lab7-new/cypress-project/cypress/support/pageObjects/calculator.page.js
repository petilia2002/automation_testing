class CalculatorPage {
  visit() {
    cy.visit("/"); // baseUrl берётся из cypress.config.js
  }

  // методы получения элементов (используем тексты label, formcontrolname и классы)
  firstInput() {
    // на основе label "Первое число"
    return cy.contains("label", "Первое число").parent().find("input");
  }

  secondInput() {
    return cy.contains("label", "Второе число").parent().find("input");
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

  calcButton() {
    return cy.get("button.calcBtn");
  }

  resultElement() {
    return cy.get(".result");
  }

  calculationId() {
    return cy.get(".meta");
  }

  // helper actions
  fillFirst(value) {
    this.firstInput().clear().type(value);
  }

  fillSecond(value) {
    this.secondInput().clear().type(value);
  }

  selectOperation(opValue) {
    this.operationSelect().select(opValue);
  }

  selectFirstSystem(sysValue) {
    this.firstNumberSystemSelect().select(sysValue);
  }

  selectSecondSystem(sysValue) {
    this.secondNumberSystemSelect().select(sysValue);
  }

  clickCalculate() {
    this.calcButton().click();
  }
}

module.exports = new CalculatorPage();
