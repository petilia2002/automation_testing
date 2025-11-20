const { When, Then } = require("@badeball/cypress-cucumber-preprocessor");
const page = require("../pageObjects/calculator.page");

// Цвет результата — используем stub ответа сервера
When("сервер возвращает результат {string}", (result) => {
  // Подменяем ответ POST /api/calculator/calculate
  cy.intercept("POST", "/api/calculator/calculate", {
    statusCode: 200,
    body: { result: result, calculationId: 12345 },
  }).as("calcCall");

  // После stub — триггерим вычисление (параметры не важны, потому что ответ stubbed)
  // Убедимся, что есть какие-то валидные значения в форме
  page.fillFirst("1");
  page.fillSecond("1");
});

When('я нажимаю "Вычислить"', () => {
  page.clickCalculate();
  cy.wait("@calcCall");
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
