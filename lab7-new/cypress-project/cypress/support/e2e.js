// e2e.js
// импортируем кастомные команды и page objects
import "./commands";

// можно добавить сюда глобальные настройки/таймауты
Cypress.on("uncaught:exception", (err, runnable) => {
  // чтобы uncaught ошибки angular не падали тестам (при необходимости)
  return false;
});
