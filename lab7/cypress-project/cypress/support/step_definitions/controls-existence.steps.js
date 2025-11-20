const { Then } = require("@badeball/cypress-cucumber-preprocessor");
const page = require("../pageObjects/calculator.page");

Then('я вижу поле "Первое число"', () => {
  page.firstInput().should("exist").and("be.visible");
});

Then('я вижу поле "Второе число"', () => {
  page.secondInput().should("exist").and("be.visible");
});

Then("я вижу селект операций", () => {
  page.operationSelect().should("exist").and("be.visible");
});

Then("я вижу селект системы счисления для первого числа", () => {
  page.firstNumberSystemSelect().should("exist").and("be.visible");
});

Then("я вижу селект системы счисления для второго числа", () => {
  page.secondNumberSystemSelect().should("exist").and("be.visible");
});

Then("я вижу поле для настройки точности результата", () => {
  page.precisionInput().should("exist").and("be.visible");
});

Then('я вижу кнопку "Вычислить"', () => {
  page.calcButton().should("exist").and("be.visible");
});
