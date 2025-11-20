# Лабораторная работа №6 – Модульное тестирование фронтенд приложений
#### Выполнили: Янин Дмитрий, Петренков Илья, гр. 6231-020402D

Учебное Single Page Application на **Angular JS** (standalone components).  
Приложение служит клиентской частью к бэкенду (**Java Spring**) и позволяет выполнять арифметические операции между двумя числами в разных системах счисления. В работе реализованы: компонент калькулятора, инпут, кастомный `ControlValueAccessor` для инпутов с валидацией по системе счисления, **директива** для окраски результата, **pipe** для контроля точности, **Unit-тесты (Jasmine + Karma)**, а также базовые CSS-стили.

## 📌 Цель работы

**Цель лабораторной работы** – в процессе выполнения заданий изучить основы *модульного тестирования* приложения на **Angular JS**, с использованием технологий **Jasmine** и **Karma**.

**В рамках выполнения работы реализованы:**

- `AppComponent` – корневой standalone компонент, рендерит `app-calculator`.
- `CalculatorComponent` – основная форма:
  - два поля ввода (в виде отдельного `NumberInputComponent`);
  - выбор операции (ADD, SUBTRACT, MULTIPLY, DIVIDE);
  - выбор системы счисления для каждого числа (BINARY, OCTAL, DECIMAL, HEXADECIMAL);
  - поле `precision` для количества знаков после запятой;
  - при выборе `DIVIDE` физически запрещён ввод нуля во втором инпуте (через вход `forbidZero` + обработку `keydown`/`paste`/`input`) и применяется валидатор.
- `NumberInputComponent`:
  - реализует `ControlValueAccessor` и `Validator`,
  - проверяет допустимые символы по системе счисления,
  - поддерживает параметр `forbidZero`.
- `ResultColorDirective` – директива, окрашивающая результат:
  - `null` / `''` – не ставит inline-стиля (т.е. `element.style.color === ''`);
  - `< 0` – `red`;
  - `== 0` – `black` (inline);
  - `> 0` – `green`.
- `PrecisionPipe` – пайп, форматирует строковый/числовой результат с заданным количеством знаков после запятой.
- Аккуратные пастельные CSS стили, адаптивная верстка.
- Unit-тесты на директиву, пайп и компоненты (Jasmine + Karma). Для тестов используется `provideHttpClient()` + `provideHttpClientTesting()` для поддержки `HttpClient`.

---

## ⚙️ Используемые технологии

- **Angular** (standalone components, v16+ совместимый стиль)
- **TypeScript**
- **Jasmine + Karma** (unit-тесты)
- **Java Spring Boot** – бэкенд (API)
- **HTML / CSS**

## 📁 Структура проекта

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

### Предварительные требования
- **Node.js** (версия 18 или выше)
- **npm** или **yarn**
- **Angular CLI** (версия 16 или выше)

### 🛠 Установка и запуск

1. **Клонирование репозитория**

   ```bash
   # Клонирование репозитория
   git clone https://git.fiit.ssau.ru/6231-020402D/automate-testing.git

   # Переключение на нужную ветку
   git checkout epic/Yanin-Petrenkov

   # Переход в директорию клиентской части
   cd frontend
    ```

2. **Установка зависимостей**

   ```bash
    npm install
    # или
    yarn install
    ```

3. **Запуск приложения**

   ```bash
    ng serve
    # или
    npm start
    ```

4. **Запуск тестов**

   ```bash
    # Запуск всех тестов
    ng test

    # Запуск с покрытием кода
    ng test --code-coverage

    # Запуск конкретного набора тестов
    ng test --include='**/*.component.spec.ts'
    ```

5. **Сборка проекта**
   ```bash
    # Собрать проект
    ng build
    ```
