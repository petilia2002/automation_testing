# Лабораторная работа №7 – е2е тестирование
#### Выполнили: Янин Дмитрий, Петренков Илья, гр. 6231-020402D

## 📋 Описание проекта
Сквозное тестирование **Angular-приложения** "Калькулятор" с использованием **Cypress** и **Cucumber** (Gherkin нотация).

## 🎯 Цель работы

**Цель лабораторной работы** – в процессе выполнения заданий ознакомиться с библиотеками для **сквозного тестирования** front-end приложений. За основу взять приложение из **6 лабораторной работы**.

**Основные задачи:**
- Освоить библиотеку для **сквозного тестирования** frontend приложений
- Реализовать тесты с использованием **Gherkin** нотации
- Применить паттерн **Page Object Model**
- Настроить **Cypress** для **E2E** тестирования

## 🛠 Технологический стек
- **Cypress** - фреймворк для E2E тестирования
- **Cucumber** - BDD фреймворк
- **Gherkin** - язык для описания тест-кейсов
- **JavaScript** - язык реализации
- **Page Object Pattern** - паттерн для организации кода

## 📁 Структура проекта

### e2e тестирование (Cypress)

- cypress-project/
    - package.json
    - package-lock.json
    - cypress.config.js
    - .gitignore
    - node_modules
    - cypress/
        - e2e/
            - features/
                - binary_operations.feature
                - decimal_operations.feature
                - octal_operations.feature
                - hexadecimal_operations.feature
                - controls-existence.feature
                - result-colors.feature
        - support/
            - e2e.js
            - commands.js
            - pageObjects/
                - calculator.page.js
            - step_definitions/
                - controls-existence.steps.js
                - result-colors.steps.js
                - operations.steps.js

### Фронтенд (Angular JS)

- frontend/
    - node_modules
    - public
    - README.md
    - .editorconfig
    - .gitignore
    - angular.json
    - package.json
    - package-lock.json
    - tsconfig.app.json
    - tsconfig.json
    - tsconfig.spec.json
    - src/
        - index.html
        - style.css
        - main.ts
        - app/
            - app.component.css
            - app.component.html
            - app.component.ts
            - app.component.spec.ts
            - app.config.js
            - models/
                - enums.ts
            - services/
                - calculator.service.spec.ts
                - calculator.service.ts
            - directives/
                - result-color.directive.spec.ts
                - result-color.directive.ts
            - pipes/
                - precision.pipe.spec.ts
                - precision.pipe.ts
            - components/
                - calculator/
                    - calculator.component.css
                    - calculator.component.html
                    - calculator.component.ts
                    - calculator.component.spec.ts
                - number-input/
                    - number-input.component.css
                    - number-input.component.html
                    - number-input.component.ts
                    - number-input.component.spec.ts

<br/>

### Бэкенд API (Java Spring)

- backend/
    - pom.xml
    - src/
        - main/
            - java/
                - calculator/
                    - controller
                        - CalculatorController.java
                    - dto
                        - CalсulationRequest.java
                        - CalсulationResponse.java
                        - HistoryRequest.java
                    - entity
                        - Calсulation.java
                        - NumberSystem.java
                        - OperationType.java
                    - repository
                        - CalсulationRepository.java
                    - service
                        - CalсulationService.java
                    - Calculator.java
                    - CalculatorApp.java
                    - CalculatorApplication.java
            - resources/
                - db.migration
                    - V1__Create_calculations_table.sql
                - application.properties
        - test/
            - java/
                - calculator/
                    - CalculatorTest.java
                    - TestDataProviders.java
                    - config
                        - AbstractIntegrationTest.java
                        - CucumberSpringConfiguration.java
                        - CustomDataTableType.java
                    - hooks
                        - DatabaseHooks.java
                    - integration
                        - CalculationTestCase.java
                        - CalculatorIntegrationTest.java
                        - DockerCheckTest.java
                        - NegativeEnumsIntegrationTest.java
                        - TestDataLoader.java
                    - model
                        - CalculationData.java
                        - HistoryFilter.java
                        - TestCalculation.java
                    - runner
                        - RunnerTest.java
                    - stepdefs
                        - CalculatorSteps.java
                        - CustomParameterTypes.java
            - resources/
                - features
                    - calculator_history.feature
                    - calculator_operations.feature
                - application-test.properties
                - test_data
                    - calculations
                        - calculation.json
                        - calculations.json
                    - expected
                        - calculation_response.json
                        - history_result.json
                - add_tests.csv
                - div_tests.csv
                - mul_tests.csv
                - sub_tests.csv
                - test_data.csv
                - test_data.yaml
    - README.md
    - .gitignore
    - target
        - cucumber.json
        - cucumber-reports.html

<br/>

## 📥 Как запустить

### ⚙️ Предварительные требования

Перед запуском убедитесь, что установлены:
- **Node.js** 16.x или выше
- **npm** 8.x или выше  
- **Java** 18 или выше
- **Maven** 3.6 или выше
- **Angular CLI** 16.x или выше

### 🚀 Последовательность запуска

#### 1. Запуск бэкенд сервера

```bash
# Перейти в папку бэкенда
cd backend

# Собрать проект Maven
mvn clean compile

# Запустить Spring Boot приложение
mvn spring-boot:run

# ИЛИ запустить через main класс
mvn exec:java -Dexec.mainClass="calculator.CalculatorApplication"
```

**Бэкенд будет доступен по адресу:** ```http://localhost:8080```

#### 2. Запуск фронтенда
```bash
# Откройте НОВЫЙ терминал и перейдите в папку фронтенда
cd frontend

# Установить зависимости (если не установлены)
npm install

# Запустить Angular development сервер
ng serve

# Или с конкретным портом
ng serve --port 4200
```

**Фронтенд будет доступен по адресу:** ```http://localhost:4200```

#### 3. Запуск E2E тестов
```bash
# Откройте НОВЫЙ терминал и перейдите в папку Cypress
cd cypress-project

# Установить зависимости (если не установлены)
npm install

# Запустить Cypress в интерактивном режиме
npx cypress open

# ИЛИ запустить конкретный тест
npx cypress run --spec "cypress/e2e/features/decimal_operations.feature"
```
