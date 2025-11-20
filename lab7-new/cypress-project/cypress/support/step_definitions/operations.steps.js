const {
  Given,
  When,
  Then,
} = require("@badeball/cypress-cucumber-preprocessor");
const page = require("../pageObjects/calculator.page");

Given("я открыл страницу калькулятора", () => {
  page.visit();
});

When("я выбираю бинарную систему для обоих операндов", () => {
  page.selectFirstSystem("BINARY");
  page.selectSecondSystem("BINARY");
});

When("я выбираю десятичную систему для обоих операндов", () => {
  page.selectFirstSystem("DECIMAL");
  page.selectSecondSystem("DECIMAL");
});

When("я выбираю восьмеричную систему для обоих операндов", () => {
  page.selectFirstSystem("OCTAL");
  page.selectSecondSystem("OCTAL");
});

When("я выбираю шестнадцатеричную систему для обоих операндов", () => {
  page.selectFirstSystem("HEXADECIMAL");
  page.selectSecondSystem("HEXADECIMAL");
});

When("я ввожу в первое число {string}", (value) => {
  page.fillFirst(value);
});

When("ввожу во второе число {string}", (value) => {
  page.fillSecond(value);
});

When("выбираю операцию {string}", (op) => {
  page.selectOperation(op);
});

When('нажимаю "Вычислить"', () => {
  page.clickCalculate();
});

Then("результат должен быть {string}", (expected) => {
  // ждём ответ от бекенда и проверяем результат в DOM
  page.resultElement().should("exist").and("contain", expected);
});
