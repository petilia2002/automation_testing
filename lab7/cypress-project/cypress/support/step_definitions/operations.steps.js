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
  page.resultElement().should("exist").and("contain", expected);
});

// Проверка ввода: HEXADECIMAL
When("я выбираю систему для первого числа {string}", (sys) => {
  page.selectFirstSystem(sys);
});

When("я выбираю систему для второго числа {string}", (sys) => {
  page.selectSecondSystem(sys);
});

When("ввожу в первое число {string}", (value) => {
  page.fillFirst(value);
});

Then("значение первого числа должно быть {string}", (expected) => {
  page.firstInput().invoke("val").should("equal", expected);
});

Then("значение второго числа должно быть {string}", (expected) => {
  page.secondInput().invoke("val").should("equal", expected);
});

// Проверка ввода: DECIMAL
When("пытаюсь ввести в первое число {string}", (value) => {
  page.firstInput().clear().type(value);
});

When("пытаюсь ввести во второе число {string}", (value) => {
  page.secondInput().clear().type(value);
});
