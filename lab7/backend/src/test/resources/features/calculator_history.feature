Feature: Получение истории вычислений
  Как пользователь
  Я хочу просматривать историю выполненных операций с фильтрацией
  Чтобы отслеживать предыдущие операции

  Background:
    Given база данных очищена

  Scenario: Получение всей истории (без фильтров)
    Given в базе есть расчеты:
      | firstNumber | firstNumberSystem | secondNumber | secondNumberSystem | operationType | result | createdAt              |
      | 10          | DECIMAL           | 5            | DECIMAL            | ADD           | 15     | 2025-10-01T12:00:00    |
      | 8           | DECIMAL           | 2            | DECIMAL            | DIVIDE        | 4      | 2025-10-02T12:00:00    |
      | 100         | DECIMAL           | 50           | DECIMAL            | SUBTRACT      | 50     | 2025-10-05T12:00:00    |
      | A           | HEXADECIMAL       | 5            | DECIMAL            | ADD           | F      | 2025-10-06T12:00:00    |
    When я запрашиваю историю вычислений без фильтров
    Then я получаю список из 4 операций

  Scenario: Передача коллекции через таблицу (несколько типов операций)
    Given в базе есть расчеты:
      | firstNumber | firstNumberSystem | secondNumber | secondNumberSystem | operationType | result | createdAt              |
      | 10          | DECIMAL           | 5            | DECIMAL            | ADD           | 15     | 2025-10-01T12:00:00    |
      | 8           | DECIMAL           | 2            | DECIMAL            | DIVIDE        | 4      | 2025-10-02T12:00:00    |
      | 100         | DECIMAL           | 50           | DECIMAL            | SUBTRACT      | 50     | 2025-10-05T12:00:00    |
      | A           | HEXADECIMAL       | 5            | DECIMAL            | ADD           | F      | 2025-10-06T12:00:00    |
    When я запрашиваю историю по типам операций:
      | operationType |
      | ADD           |
      | SUBTRACT      |
    Then я получаю список из 3 операций
    And все операции имеют тип из таблицы:
      | operationType |
      | ADD           |
      | SUBTRACT      |

  Scenario: Передача через класс (каждая строка — фильтр с expectedCount)
    Given в базе есть расчеты:
      | firstNumber | firstNumberSystem | secondNumber | secondNumberSystem | operationType | result | createdAt              |
      | 10          | DECIMAL           | 5            | DECIMAL            | ADD           | 15     | 2025-10-01T12:00:00    |
      | 8           | DECIMAL           | 2            | DECIMAL            | DIVIDE        | 4      | 2025-10-02T12:00:00    |
      | 100         | DECIMAL           | 50           | DECIMAL            | SUBTRACT      | 50     | 2025-10-05T12:00:00    |
      | A           | HEXADECIMAL       | 5            | DECIMAL            | ADD           | F      | 2025-10-06T12:00:00    |
    When я запрашиваю историю по фильтрам (классом):
      | operationType | startDate             | endDate               | expectedCount |
      | ADD           | 2025-10-01T00:00:00   | 2025-10-07T00:00:00   | 2             |
      | DIVIDE        | 2025-10-02T00:00:00   | 2025-10-03T00:00:00   | 1             |
    Then результаты для каждого фильтра совпадают с ожидаемым

  Scenario: Кастомный разделитель для передачи фильтра
    Given в базе есть расчеты:
      | firstNumber | firstNumberSystem | secondNumber | secondNumberSystem | operationType | result | createdAt              |
      | 10          | DECIMAL           | 5            | DECIMAL            | ADD           | 15     | 2025-10-01T12:00:00    |
      | 8           | DECIMAL           | 2            | DECIMAL            | DIVIDE        | 4      | 2025-10-02T12:00:00    |
      | 100         | DECIMAL           | 50           | DECIMAL            | SUBTRACT      | 50     | 2025-10-05T12:00:00    |
      | A           | HEXADECIMAL       | 5            | DECIMAL            | ADD           | F      | 2025-10-06T12:00:00    |
    When я запрашиваю историю с фильтром: "ADD;DECIMAL;DECIMAL"
    Then я получаю список из 1 операций
    And все операции имеют тип ADD

  Scenario: Несколько аргументов (операция + период)
    Given в базе есть расчеты:
      | firstNumber | firstNumberSystem | secondNumber | secondNumberSystem | operationType | result | createdAt              |
      | 10          | DECIMAL           | 5            | DECIMAL            | ADD           | 15     | 2025-10-01T12:00:00    |
      | 8           | DECIMAL           | 2            | DECIMAL            | DIVIDE        | 4      | 2025-10-02T12:00:00    |
      | 100         | DECIMAL           | 50           | DECIMAL            | SUBTRACT      | 50     | 2025-10-05T12:00:00    |
      | A           | HEXADECIMAL       | 5            | DECIMAL            | ADD           | F      | 2025-10-06T12:00:00    |
    When я запрашиваю историю с типом ADD с "2025-10-01T00:00:00" по "2025-10-07T00:00:00"
    Then я получаю список из 2 операций