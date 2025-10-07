# Лабораторная работа №3 - BDD-тестирование
#### Выполнили: Янин Дмитрий, Петренков Илья, гр. 6231-020402D

В рамках лабораторной работы осуществляется разработка поведенческих тестов с применением методологии BDD и фреймворка Cucumber для верификации функциональности калькулятора.

## 📌 Цель работы

Цель лабораторной работы — освоить методологию **поведенческого тестирования (Behavior-Driven Development, BDD)**  
и научиться применять её для тестирования бизнес-логики REST-сервиса на Java.

В рамках выполнения работы необходимо:

1. Освоить принципы написания **BDD-сценариев** на языке **Gherkin**.
2. Научиться реализовывать шаги сценариев в **Cucumber (Step Definitions)** на Java.
3. Реализовать сервис калькулятора, поддерживающий различные операции и системы счисления.
4. Обеспечить сохранение результатов вычислений в базу данных и их последующее получение.
5. Реализовать тестовые сценарии для проверки:
    - выполнения математических операций;
    - корректного сохранения результатов;
    - получения истории операций с фильтрацией по параметрам.
6. Освоить работу с таблицами (`DataTable`), пользовательскими типами (`@ParameterType`) и классами (`List<CustomClass>`).
7. Настроить автоматическое выполнение сценариев и проверку результатов с помощью **JUnit 5**.

## 🧩 Основные задачи
1. Реализовать сервис `CalculatorService` с поддержкой:
    - различных систем счисления (`BINARY`, `DECIMAL`, `HEXADECIMAL` и др.);
    - базовых операций (`ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`);
    - сохранения результатов вычислений в базу данных (`CalculationRepository`);
    - получения истории операций с фильтрацией по типу операции, системе счисления и диапазону дат.

2. Разработать **BDD-тесты** на основе `Cucumber`:
    - описать сценарии на языке **Gherkin**;
    - реализовать шаги (`Step Definitions`) на Java;
    - использовать хуки и preconditions для подготовки данных.

---

## 🧠 Реализованные user stories

### 1. Выполнение математических операций
**Feature:** Сложение, вычитание, умножение чисел  
**Реализованные типы сценариев:**
- Сценарий с несколькими аргументами (`Scenario Outline`);
- Передача коллекций через таблицу (`DataTable`);
- Передача коллекций через класс (`List<CalculationData>`);
- Передача коллекций через кастомный разделитель (`@ParameterType(".*")`);
- Проверка пустого списка чисел;
- Поддержка различных систем счисления (binary, decimal, hexadecimal).

**Основной файл:**  
`src/test/resources/features/calculator_operations.feature`

---

### 2. Получение истории вычислений
**Feature:** История вычислений с фильтрацией по типу операции и диапазону дат  
**Реализованные типы сценариев:**
- Инициализация БД с помощью шага `Given` (табличная передача данных);
- Получение полной истории без фильтров;
- Фильтрация по типу операции;
- Фильтрация по диапазону дат;
- Передача фильтров через класс (`HistoryFilter`);
- Проверка количества найденных записей по ожидаемому результату.

**Основной файл:**  
`src/test/resources/features/calculator_history.feature`

---

## 📁 Структура проекта

- project/
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

---

## ⚙️ Используемые технологии

| Технология                 | Назначение                          |
|----------------------------|-------------------------------------|
| **Java 21+**               | Основной язык реализации приложения |
| **Spring Boot**            | Контекст приложения и DI            |
| **Cucumber (io.cucumber)** | BDD-фреймворк для тестирования      |
| **JUnit 5**                | Тестовый раннер                     |
| **Docker Desktop**         | Для запуска BDD-тестов              |
| **Maven / Gradle**         | Сборка проекта                      |

---

## 🔬 Особенности реализации
- Каждый сценарий **независим**, база данных очищается перед тестом и наполняется через `Given`-таблицы.
- Для фильтрации истории реализован класс `HistoryFilter`, используемый в шагах `When я запрашиваю историю по фильтрам (классом):`.
- Используются хуки (`@Before`, `@After`) для сброса состояния БД, если требуется.
- Проверки реализованы с помощью `assertEquals`, `assertNotNull` и `assertFalse` (JUnit 5).

---

## 🧪 Примеры сценариев

### Простые операции
```gherkin
Scenario: Сложение двух чисел
When я складываю числа 5 и 3
Then результат должен быть "8"
```

### Передача коллекций через таблицу
```gherkin
Scenario: Сложение нескольких чисел через таблицу
When я складываю следующие числа:
| 10 |
| 5  |
| 3  |
Then результат должен быть "18"
```

### Передача коллекций через кастомный разделитель
```gherkin
Scenario: Сложение чисел с кастомным разделителем
When я складываю числа: 1;2;3;4;5
Then результат должен быть "15"
```

## ⚙️ Как запустить

## Предварительные требования

- **Java JDK 21+**
- **Maven 3.6+**
- **Docker Desktop** (для запуска тестов)
- **Git** (для клонирования репозитория)

## 📥 Запуск проекта

### 1. Клонирование репозитория
```bash
git clone <url-репозитория>
cd automate-testing
```

### 2. Проверка установки Java
```bash
java -version
```

### 3. Запуск RestAPI
Приложение можно запустить, выполнив класс `CalculatorApplication`:

**Через IDE:**
- Откройте класс `/calculator/CalculatorApplication`
- Нажмите правой кнопкой → "Run CalculatorApp"
- Или используйте горячие клавиши (Ctrl+Shift+F10 в IntelliJ IDEA)

### 4. Запуск тестов
Тесты можно запустить, выполнив класс `RunnerTest`:

**Через IDE:**
- Откройте класс `/calculator/runner/RunnerTest`
- Нажмите правой кнопкой → "Run RunnerTest"
- Или используйте горячие клавиши (Ctrl+Shift+F10 в IntelliJ IDEA)