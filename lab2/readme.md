# Лабораторная работа №2 - Интеграционное тестирование
#### Выполнили: Янин Дмитрий, Петренков Илья, гр. 6231-020402D

Spring Boot приложение-калькулятор, выполняющее операции сложения, вычитания, умножения и деления чисел в разных системах счисления. Оно сохраняет результаты вычислений в PostgreSQL и предоставляет REST API для получения новых результатов и истории операций.

## 📌 Цель работы
На основе первой лабораторной работы (реализация вычислений с поддержкой разных систем счисления) создать приложение с использованием **Spring Boot**, которое:
- хранит результаты вычислений в базе данных;
- предоставляет REST API для выполнения операций и получения истории;
- использует миграции (Flyway) для управления схемой БД;
- покрыто интеграционными тестами с использованием **Testcontainers** и загрузкой тестовых данных из JSON.

---

## ✨ Реализованный функционал

1. **База данных (PostgreSQL)**
    - Таблица `calculations` хранит:
        - первое число и систему счисления,
        - второе число и систему счисления,
        - тип операции (сложение, вычитание, умножение, деление),
        - дату и время вычисления,
        - результат операции.
    - Все DDL-операции оформлены в виде **Flyway миграций**.

2. **REST API**
    - `POST /api/calculator/calculate`  
      Выполняет операцию над двумя числами (в разных системах счисления), сохраняет результат в БД и возвращает его.
    - `GET /api/calculator/history`  
      Возвращает историю вычислений, с возможностью фильтрации по:
        - типу операции (`operationType`),
        - системе счисления (`firstNumberSystem`, `secondNumberSystem`),
        - дате/времени выполнения.

3. **Интеграционные тесты**
    - Запускаются на реальной PostgreSQL с использованием **Testcontainers**.
    - Перед каждым тестом тестовые данные загружаются из JSON-файлов через `ObjectMapper`.
    - Проверяется:
        - корректность вычислений,
        - сохранение записей в БД,
        - фильтрация истории,
        - обработка разных типов операций.

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
            - resources/
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
- Откройте класс `CalculatorApplication`
- Нажмите правой кнопкой → "Run CalculatorApp"
- Или используйте горячие клавиши (Ctrl+Shift+F10 в IntelliJ IDEA)

### 4. Запуск тестов
Тесты можно запустить, выполнив класс `CalculatorIntegrationTest`:

**Через IDE:**
- Откройте класс `CalculatorIntegrationTest`
- Нажмите правой кнопкой → "Run CalculatorIntegrationTest"
- Или используйте горячие клавиши (Ctrl+Shift+F10 в IntelliJ IDEA)