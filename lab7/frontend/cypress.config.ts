import { defineConfig } from 'cypress';
import createBundler from '@bahmutov/cypress-esbuild-preprocessor';
import createEsbuildPlugin from '@badeball/cypress-cucumber-preprocessor/esbuild';
import { addCucumberPreprocessorPlugin } from '@badeball/cypress-cucumber-preprocessor';

export default defineConfig({
  e2e: {
    specPattern: 'cypress/e2e/**/*.feature',
    baseUrl: 'http://localhost:4200',
    async setupNodeEvents(on, config) {
      // обязательно регистрируем cucumber preprocessor
      await addCucumberPreprocessorPlugin(on, config);

      // регистрируем esbuild-бандлер, добавляя плагин cucumber->esbuild
      on(
        'file:preprocessor',
        createBundler({
          plugins: [createEsbuildPlugin(config)],
        })
      );

      // вернуть конфиг обязательно
      return config;
    },
  },
});
