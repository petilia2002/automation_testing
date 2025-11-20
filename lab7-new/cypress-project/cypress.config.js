// cypress.config.js
const { defineConfig } = require("cypress");
const createBundler = require("@bahmutov/cypress-esbuild-preprocessor");
const {
  addCucumberPreprocessorPlugin,
} = require("@badeball/cypress-cucumber-preprocessor");
// Важно: взять именованный экспорт createEsbuildPlugin
const {
  createEsbuildPlugin,
} = require("@badeball/cypress-cucumber-preprocessor/esbuild");

module.exports = defineConfig({
  e2e: {
    specPattern: "cypress/e2e/**/*.feature",
    baseUrl: "http://localhost:4200",

    // setupNodeEvents может быть async — addCucumberPreprocessorPlugin иногда возвращает промис
    async setupNodeEvents(on, config) {
      // устанавливаем cucumber preprocessor
      await addCucumberPreprocessorPlugin(on, config);

      // регистрируем bundler esbuild + cucumber
      const bundler = createBundler({
        plugins: [createEsbuildPlugin(config)],
      });
      on("file:preprocessor", bundler);

      return config;
    },

    supportFile: "cypress/support/e2e.js",
    video: false,
  },
});
