// cypress/support/cucumber.d.ts
// Позволяет TypeScript находить модуль '@badeball/cypress-cucumber-preprocessor/steps'
// и не ругаться на импорт Given/When/Then в step definitions.

declare module '@badeball/cypress-cucumber-preprocessor/steps' {
  // Общий тип для колбэков шагов
  type StepFunction = (text: string, ...args: any[]) => void;
  type StepDef = (pattern: string, fn: (...args: any[]) => void) => void;

  export const Given: StepDef;
  export const When: StepDef;
  export const Then: StepDef;
}
