// calculator.steps.js
const {
  Given,
  When,
  Then,
} = require("@badeball/cypress-cucumber-preprocessor");
const page = require("../../support/pageObjects/calculator.page");

Given("я открыл страницу калькулятора", () => {
  page.visit();
});

Then('я вижу поле "Первое число"', () => {
  page.firstInput().should("exist").and("be.visible");
});

Then('я вижу поле "Второе число"', () => {
  page.secondInput().should("exist").and("be.visible");
});

Then("я вижу селект операций", () => {
  page.operationSelect().should("exist").and("be.visible");
});

Then('я вижу кнопку "Вычислить"', () => {
  page.calcButton().should("exist").and("be.visible");
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

// Проверка ввода: DECIMAL
When("я выбираю систему для первого числа {string}", (sys) => {
  page.selectFirstSystem(sys);
});

When("пытаюсь ввести в первое число {string}", (value) => {
  page.firstInput().clear().type(value);
});

Then("значение первого числа должно быть {string}", (expected) => {
  page.firstInput().invoke("val").should("equal", expected);
});

// DIVIDE — запрет ввода нуля во второй инпут
When("пытаюсь ввести во второе число {string}", (value) => {
  page.secondInput().clear().type(value);
});

Then("значение второго числа должно быть {string}", (expected) => {
  page.secondInput().invoke("val").should("equal", expected);
});

// HEXADECIMAL ввод букв
Then('значение первого числа должно быть "1aF"', () => {
  page.firstInput().invoke("val").should("equal", "1aF");
});

// Цвет результата — используем stub ответа сервера
When("сервер возвращает результат {string} (через stub)", (result) => {
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
