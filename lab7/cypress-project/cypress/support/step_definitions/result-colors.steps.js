const {
  Given,
  When,
  Then,
} = require("@badeball/cypress-cucumber-preprocessor");
const page = require("../pageObjects/calculator.page");

Given("я открыл главную страницу", () => {
  page.visit();
});

When("я ввожу первое число {string}", (value) => {
  page.fillFirst(value);
});

When("я ввожу второе число {string}", (value) => {
  page.fillSecond(value);
});

When("я выбираю операцию {string}", (op) => {
  page.selectOperation(op);
});

When('я нажимаю кнопку "Вычислить"', () => {
  page.clickCalculate();
});

Then("цвет результата должен быть {string}", (colorName) => {
  const expected = {
    red: "rgb(255, 0, 0)",
    black: "rgb(0, 0, 0)",
    green: "rgb(0, 128, 0)",
  }[colorName];

  page
    .resultElement()
    .should("exist")
    .and(($el) => {
      const cs = window.getComputedStyle($el[0]);
      expect(cs.color).to.eq(expected);
    });
});
