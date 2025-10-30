# Лабораторная работа 5 - Тестирование API
#### Выполнили: Янин Дмитрий, Петренков Илья, гр. 6231-020402D

## 📌 Цель работы
В процессе выполнения заданий познакомиться с возможностью автоматизированного тестирования API в **Postman**.

## ✨ Тестируемое приложение
**REST API** калькулятора для выполнения арифметических операций с
сохранением их в базу данных и получением истории по всем проведенным операциям.

### 🚀 Ключевые эндпоинты API

#### 🧮 `POST /api/calculator/calculate`
**Вычислительный маршрут** - выполняет комплексные арифметические операции между числами в различных системах счисления.

**Функциональность:**
- 🔢 **Поддержка multiple систем счисления**: *BINARY, DECIMAL, OCTAL, HEXADECIMAL*.
- ➕ **Арифметические операции**: *ADD, SUBTRACT, MULTIPLY, DIVIDE*.
- 💾 **Автоматическое сохранение** результатов в базе данных.
- 🔄 **Конвертация результатов** между системами счисления.
- ✅ **Валидация входных данных** и обработка ошибок.

**Пример запроса:**
```json
{
    "firstNumber": "110",
    "firstNumberSystem": "BINARY", 
    "secondNumber": "100",
    "secondNumberSystem": "BINARY",
    "operationType": "MULTIPLY"
}
```

#### 🧮 `GET /api/calculator/history`
Получение истории вычислений - предоставляет детализированную историю операций
с расширенными возможностями фильтрации.

**Возможности фильтрации:**
- 🎯 **По типу операции**: *operationType (ADD, SUBTRACT, MULTIPLY, DIVIDE)*.
- 🔢 **По системе счисления**: *firstNumberSystem, secondNumberSystem*.
- 📅 **По временным интервалам**: дата и время выполнения операции.
- 🎚️ **Комбинированные фильтры для точного поиска**.
- 📈 **Пагинация и сортировка результатов**.

## 🚀 Инструкция по запуску

### Предварительные требования
- Java 21 или выше
- Postman
- Maven 3.6+ (для сборки проекта)
- DBeaver или другой SQL-клиент (для управления БД)

### 1. Запуск сервера приложения

#### Сборка и запуск через Maven:
```bash
# Клонирование репозитория (если необходимо)
git clone <repository-url>
cd calculator-lab

# Сборка приложения
mvn clean package

# Запуск сервера
java -jar target/calculator-app-1.0.0.jar
```

#### Альтернативный запуск в IDE:
```bash
# Импортируйте проект как Maven проект в IntelliJ IDEA или Eclipse
# Найдите главный класс: CalculatorApplication.java
# Запустите метод main()
```

### 2. Запуск тестов через Postman

#### 🚀 Импорт коллекции и окружений
1. Откройте **Postman** → **Import → Files**.
2. Выберите:
    - `./postman/Testing - lab5.postman_collection.json` — коллекция запросов
    - `./postman/dev.postman_environment.json` и `./postman/local.postman_environment.json` — окружения
3. Убедитесь, что выбранное окружение активно и URL формируется через переменные.

---

#### 🔁 Последовательный запуск тестов
##### В Postman Runner
1. Откройте **Runner** (пункт “Run” в меню actions у коллекции).
2. Выберите:
    - Environment: `local` или `dev` (предпочтительно `local`)
    - Iterations: `1000`
3. Нажмите **Start Run** и дождитесь завершения.

##### Через Newman (CLI)
```bash
newman run Testing-lab5.postman_collection.json -e local.postman_environment.json --iteration-count 1000 -r cli,json --reporters-options "json.export=run-sequential.json"
```

#### ⚡ Параллельный запуск тестов
##### В Postman (через Promise.all)
1. Перейдите в коллекцию `Testing-lab5` и откройте запрос `Parallel Load Test`.
2. Нажмите кнопку `Send` для запуска параллельных тестов.
3. Отслеживайте статус выполнения тестов в **консоли** Postman.
4. Посмотрите результат в переменной окружения `parallel_test_result`.

##### Через Newman (CLI)
```bash
newman run Testing-lab5.postman_collection.json -n 1000 --parallel 10
```
## 🧪 Ход выполнения работы

### 1. Создание окружений (Environments)

Созданы два окружения: **local** и **dev**.  
В каждом заданы переменные:

| Переменная | Пример значения | Назначение |
|-------------|----------------|-------------|
| `protocol`  | `http`          | Протокол подключения |
| `host`      | `localhost`     | Хост сервера |
| `port`      | `8080`          | Порт сервера |
| `baseUrl`   | `auto`          | Формируется скриптом |

📸 **Скриншот:**

<div align="center">
  <img src="postman_screens/task1.png" width="800" alt="График пропускной способности">
  <br>
  <em>Рисунок 1 - Создание окружения </em>
</div>

Пример содержимого `local.postman_environment.json`:
```json
{
  "name": "local",
  "values": [
    {"key":"protocol","value":"http"},
    {"key":"host","value":"localhost"},
    {"key":"port","value":"8080"},
    {"key":"baseUrl","value":""}
  ]
}
```

### 2. Создание коллекции и добавление запросов

Была создана коллекция **Testing – lab5**, в которую добавлены следующие запросы для проверки работы калькулятора:

| №  | Название запроса | Метод | Назначение |
|----|------------------|--------|-------------|
| 1  | **ADD**          | POST   | Получение результата сложения двух чисел |
| 2  | **SUBTRACT**     | POST   | Получение результата вычитания |
| 3  | **MULTIPLY**     | POST   | Получение результата умножения |
| 4  | **DIVIDE**       | POST   | Получение результата деления |
| 5  | **HISTORY**      | GET    | Получение истории всех вычислений за определённый период времени |


📸 **Скриншот интерфейса Postman с коллекцией:**

<div align="center">
  <img src="postman_screens/task2.png" width="800" alt="График пропускной способности">
  <br>
  <em>Рисунок 2 - Создание коллекции </em>
</div>

---

#### Пример запроса

**POST /calculate (ADD)**  
_Пример тела запроса:_
```json
{
    "firstNumber": "1010",
    "firstNumberSystem": "BINARY",
    "secondNumber": "110",
    "secondNumberSystem": "BINARY",
    "operationType": "ADD"
}
```

