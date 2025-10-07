// src/test/java/calculator/stepdefs/CalculatorSteps.java
package calculator.stepdefs;

import calculator.dto.CalculationResponse;
import calculator.dto.HistoryRequest;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import calculator.service.CalculatorService;
import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import calculator.dto.CalculationRequest;
import calculator.model.CalculationData;
import calculator.model.HistoryFilter;
import calculator.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import io.cucumber.java.*;

import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorSteps {

    private final CalculatorService calculatorService;
    private final CalculationRepository calculationRepository;

    private List<CalculationResponse> createdResponses = new ArrayList<>();
    private List<CalculationData> inputData = new ArrayList<>();

    private String result;
    private List<Calculation> historyResults = new ArrayList<>();
    private Map<HistoryFilter, List<Calculation>> historyByFilter = new LinkedHashMap<>();

    private final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    public CalculatorSteps(CalculatorService calculatorService,
                           CalculationRepository calculationRepository) {
        this.calculatorService = calculatorService;
        this.calculationRepository = calculationRepository;
    }

    // ---------------- hooks ----------------
    @Before
    public void beforeScenario() {
        calculationRepository.deleteAll();
        createdResponses.clear();
        inputData.clear();
        historyResults.clear();
        historyByFilter.clear();
    }

    @After
    public void afterScenario() {
        calculationRepository.deleteAll();
    }

    // ---------------- утилиты ----------------
    private List<String> normalizeSingleColumn(DataTable dt) {
        List<String> list = dt.asList(String.class);
        if (!list.isEmpty() && list.get(0).equalsIgnoreCase("operationType")) {
            return list.subList(1, list.size());
        }
        return list;
    }

    @Given("база данных очищена")
    public void clearDatabase() {
        calculationRepository.deleteAll();
    }

    @When("я складываю числа {int} и {int}")
    public void addTwoNumbers(int a, int b) {
        CalculationRequest request = new CalculationRequest(
                String.valueOf(a),
                NumberSystem.DECIMAL,
                String.valueOf(b),
                NumberSystem.DECIMAL,
                OperationType.ADD
        );
        result = calculatorService.saveCalculationWithResponse(request).getResult();
    }

    @When("я вычитаю число {int} из {int}")
    public void subtractNumbers(int a, int b) {
        CalculationRequest request = new CalculationRequest(
                String.valueOf(b), // из какого числа вычитаем
                NumberSystem.DECIMAL,
                String.valueOf(a), // какое число вычитаем
                NumberSystem.DECIMAL,
                OperationType.SUBTRACT
        );
        result = calculatorService.saveCalculationWithResponse(request).getResult();
    }

    @Then("результат должен быть {string}")
    public void verifyResultString(String expected) {
        assertEquals(expected, result);
    }

    // Дополнительные шаги для работы с разными системами счисления
    @When("я складываю двоичное число {string} и десятичное число {int}")
    public void addBinaryAndDecimal(String binary, int decimal) {
        CalculationRequest request = new CalculationRequest(
                binary,
                NumberSystem.BINARY,  // первый аргумент - двоичный
                String.valueOf(decimal),
                NumberSystem.DECIMAL, // второй аргумент - десятичный
                OperationType.ADD
        );
        result = calculatorService.saveCalculationWithResponse(request).getResult();
        // Результат будет в BINARY, так как первый аргумент в BINARY
    }

    @When("я складываю следующие числа:")
    public void addMultipleNumbers(DataTable dataTable) {
        List<String> numbers = dataTable.asList(String.class);

        // Фильтруем только числовые значения
        List<String> numbersToAdd = numbers.stream()
                .filter(num -> num.matches("\\d+")) // оставляем только цифры
                .collect(Collectors.toList());

        if (numbersToAdd.isEmpty()) {
            result = "0";
            return;
        }

        // Складываем числа последовательно
        String currentResult = numbersToAdd.get(0);

        for (int i = 1; i < numbersToAdd.size(); i++) {
            CalculationRequest request = new CalculationRequest(
                    currentResult,
                    NumberSystem.DECIMAL,
                    numbersToAdd.get(i),
                    NumberSystem.DECIMAL,
                    OperationType.ADD
            );
            currentResult = calculatorService.saveCalculationWithResponse(request).getResult();
        }

        result = currentResult;
    }

    @When("я складываю числа: {semicolonSeparatedList}")
    public void sumAllNumbers(List<String> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            result = "0";
            return;
        }

        System.out.println("Получены числа: " + numbers);

        if (numbers.size() == 1) {
            result = numbers.get(0);
            return;
        }

        String currentResult = numbers.get(0);

        for (int i = 1; i < numbers.size(); i++) {
            CalculationRequest request = new CalculationRequest(
                    currentResult,
                    NumberSystem.DECIMAL,
                    numbers.get(i),
                    NumberSystem.DECIMAL,
                    OperationType.ADD
            );
            currentResult = calculatorService.saveCalculationWithResponse(request).getResult();
        }

        result = currentResult;
        System.out.println("Сумма чисел " + numbers + " = " + result);
    }
    @When("я складываю числа:")
    public void sumNoNumbers() {
        // Для пустого списка просто передаем пустой список
        sumAllNumbers(List.of());
    }

    @When("я выполняю {word} с числами {int} и {int}")
    public void performOperation(String operation, int number1, int number2) {
        CalculationRequest request;

        switch (operation.toLowerCase()) {
            case "сложение" -> request = new CalculationRequest(
                    String.valueOf(number1),
                    NumberSystem.DECIMAL,
                    String.valueOf(number2),
                    NumberSystem.DECIMAL,
                    OperationType.ADD
            );
            case "вычитание" -> request = new CalculationRequest(
                    String.valueOf(number1),
                    NumberSystem.DECIMAL,
                    String.valueOf(number2),
                    NumberSystem.DECIMAL,
                    OperationType.SUBTRACT
            );
            case "умножение" -> request = new CalculationRequest(
                    String.valueOf(number1),
                    NumberSystem.DECIMAL,
                    String.valueOf(number2),
                    NumberSystem.DECIMAL,
                    OperationType.MULTIPLY
            );
            default -> throw new IllegalArgumentException("Неизвестная операция: " + operation);
        }

        result = calculatorService.saveCalculationWithResponse(request).getResult();
        System.out.println(number1 + " " + operation + " " + number2 + " = " + result);
    }

    @Then("результат должен быть {int}")
    public void verifyResultInteger(Integer expected) {
        assertEquals(String.valueOf(expected), result);
    }

    @When("я создаю расчеты с параметрами:")
    public void createCalculationsWithParameters(List<CalculationData> calculations) {
        inputData = calculations;
        createdResponses.clear();

        for (CalculationData data : calculations) {
            System.out.println("Создаю расчет: " + data);

            CalculationRequest request = new CalculationRequest(
                    data.getFirstNumber(),
                    data.getFirstNumberSystem(),
                    data.getSecondNumber(),
                    data.getSecondNumberSystem(),
                    data.getOperationType()
            );

            CalculationResponse response = calculatorService.saveCalculationWithResponse(request);
            createdResponses.add(response);
        }
    }

    @Then("все расчеты должны быть успешно созданы")
    public void allCalculationsShouldBeCreated() {
        assertNotNull(createdResponses);
        assertFalse(createdResponses.isEmpty(), "Не было создано ни одного расчета!");
        System.out.println("✅ Всего создано расчетов: " + createdResponses.size());
    }

    @Then("результаты должны соответствовать ожиданиям")
    public void resultsShouldMatchExpectations() {
        assertEquals(inputData.size(), createdResponses.size(),
                "Количество расчетов и количество результатов не совпадает");

        for (int i = 0; i < inputData.size(); i++) {
            var expected = inputData.get(i);
            var actual = createdResponses.get(i);

            System.out.printf("➡ Проверка %d: %s %s %s = %s (ожидалось %s)%n",
                    i + 1,
                    expected.getFirstNumber(),
                    expected.getOperationType(),
                    expected.getSecondNumber(),
                    actual.getResult(),
                    expected.getExpectedResult()
            );

            assertEquals(expected.getExpectedResult(), actual.getResult(),
                    String.format("Ошибка в расчете #%d: ожидалось %s, получено %s",
                            i + 1, expected.getExpectedResult(), actual.getResult()));
        }

        System.out.println("✅ Все результаты соответствуют ожиданиям!");
    }

    // ---------------- DataTableType для HistoryFilter (преобразование строки в объект) ----------------
    @DataTableType
    public HistoryFilter historyFilterEntry(Map<String, String> entry) {
        HistoryFilter f = new HistoryFilter();

        if (entry.containsKey("operationType") && entry.get("operationType") != null && !entry.get("operationType").isEmpty()) {
            f.setOperationType(OperationType.valueOf(entry.get("operationType")));
        }
        if (entry.containsKey("firstNumberSystem") && entry.get("firstNumberSystem") != null && !entry.get("firstNumberSystem").isEmpty()) {
            f.setFirstNumberSystem(NumberSystem.valueOf(entry.get("firstNumberSystem")));
        }
        if (entry.containsKey("secondNumberSystem") && entry.get("secondNumberSystem") != null && !entry.get("secondNumberSystem").isEmpty()) {
            f.setSecondNumberSystem(NumberSystem.valueOf(entry.get("secondNumberSystem")));
        }
        if (entry.containsKey("startDate") && entry.get("startDate") != null && !entry.get("startDate").isEmpty()) {
            f.setStartDate(LocalDateTime.parse(entry.get("startDate"), dtf));
        }
        if (entry.containsKey("endDate") && entry.get("endDate") != null && !entry.get("endDate").isEmpty()) {
            f.setEndDate(LocalDateTime.parse(entry.get("endDate"), dtf));
        }
        if (entry.containsKey("expectedCount") && entry.get("expectedCount") != null && !entry.get("expectedCount").isEmpty()) {
            f.setExpectedCount(Integer.valueOf(entry.get("expectedCount")));
        }
        return f;
    }

    // ---------------- Сценарии истории ----------------

    @Given("в базе есть расчеты:")
    public void givenCalculationsExist(DataTable dataTable) {
        calculationRepository.deleteAll();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            Calculation calculation = new Calculation(
                    row.get("firstNumber"),
                    NumberSystem.valueOf(row.get("firstNumberSystem")),
                    row.get("secondNumber"),
                    NumberSystem.valueOf(row.get("secondNumberSystem")),
                    OperationType.valueOf(row.get("operationType")),
                    row.get("result"),
                    LocalDateTime.parse(row.get("createdAt"))
            );
            calculationRepository.save(calculation);
        }
        System.out.println("✅ В базу добавлено " + rows.size() + " расчетов");
    }

    // 1) вся история без фильтров
    @When("я запрашиваю историю вычислений без фильтров")
    public void getHistoryWithoutFilters() {
        HistoryRequest req = new HistoryRequest();
        historyResults = calculatorService.getCalculationHistory(req);
    }

    @Then("я получаю список из {int} операций")
    public void verifyHistorySize(int expectedCount) {
        assertNotNull(historyResults);
        assertEquals(expectedCount, historyResults.size(),
                "Ожидаемое количество операций не совпало");
    }

    // 2) передача коллекции через таблицу (operationType column)
    @When("я запрашиваю историю по типам операций:")
    public void getHistoryByOperationTypes(DataTable dt) {
        List<String> ops = normalizeSingleColumn(dt);
        // агрегируем результаты по всем запрошенным типам
        historyResults = new ArrayList<>();
        for (String opStr : ops) {
            HistoryRequest req = new HistoryRequest();
            req.setOperationType(OperationType.valueOf(opStr));
            List<Calculation> res = calculatorService.getCalculationHistory(req);
            historyResults.addAll(res);
        }
    }

    @Then("все операции имеют тип из таблицы:")
    public void allOperationsHaveTypeFromTable(DataTable dt) {
        List<String> ops = normalizeSingleColumn(dt);
        Set<OperationType> allowed = ops.stream().map(OperationType::valueOf).collect(Collectors.toSet());
        assertFalse(historyResults.isEmpty(), "Ожидалось, что найдутся операции");
        for (Calculation c : historyResults) {
            assertTrue(((Set<?>) allowed).contains(c.getOperationType()),
                    "Найденo недопустимое значение operationType: " + c.getOperationType());
        }
    }

    // 3) передача через класс (каждая строка — фильтр с expectedCount)
    @When("я запрашиваю историю по фильтрам \\(классом):")
    public void getHistoryByFiltersClass(List<HistoryFilter> filters) {
        historyByFilter.clear();
        for (HistoryFilter f : filters) {
            HistoryRequest req = new HistoryRequest();
            req.setOperationType(f.getOperationType());
            req.setFirstNumberSystem(f.getFirstNumberSystem());
            req.setSecondNumberSystem(f.getSecondNumberSystem());
            req.setStartDate(f.getStartDate());
            req.setEndDate(f.getEndDate());

            List<Calculation> res = calculatorService.getCalculationHistory(req);
            historyByFilter.put(f, res);
        }
    }

    @Then("результаты для каждого фильтра совпадают с ожидаемым")
    public void verifyResultsForEachFilter() {
        assertFalse(historyByFilter.isEmpty(), "Не было выполнено ни одного запроса по фильтрам");
        for (Map.Entry<HistoryFilter, List<Calculation>> e : historyByFilter.entrySet()) {
            HistoryFilter filter = e.getKey();
            List<Calculation> actual = e.getValue();
            Integer expected = filter.getExpectedCount();
            assertNotNull(expected, "В таблице филтров не указан expectedCount для " + filter);
            assertEquals(expected.intValue(), actual.size(),
                    String.format("Для фильтра %s ожидали %d записей, получили %d",
                            filter, expected, actual.size()));
        }
    }

    // 4) кастомный делимитер (использует уже зарегистрированный @ParameterType semicolonSeparatedList)
    @When("я запрашиваю историю с фильтром: {semicolonSeparatedList}")
    public void getHistoryWithDelimitedFilter(List<String> parts) {
        // ожидаем: [operationType, firstNumberSystem, secondNumberSystem]
        if (parts.size() < 3) {
            throw new IllegalArgumentException("Ожидался формат: OP;FIRST_SYS;SECOND_SYS");
        }
        HistoryRequest req = new HistoryRequest();
        req.setOperationType(OperationType.valueOf(parts.get(0)));
        req.setFirstNumberSystem(NumberSystem.valueOf(parts.get(1)));
        req.setSecondNumberSystem(NumberSystem.valueOf(parts.get(2)));

        historyResults = calculatorService.getCalculationHistory(req);
    }

    @Then("все операции имеют тип {word}")
    public void allOperationsHaveType(String op) {
        OperationType expected = OperationType.valueOf(op);
        assertFalse(historyResults.isEmpty(), "Ожидалось ненулевое количество операций");
        for (Calculation c : historyResults) {
            assertEquals(expected, c.getOperationType(), "Найдено несоответствие типа операции");
        }
    }

    // 5) несколько аргументов (операция + даты)
    @When("я запрашиваю историю с типом {word} с {string} по {string}")
    public void getHistoryByTypeAndPeriod(String operation, String start, String end) {
        HistoryRequest req = new HistoryRequest();
        req.setOperationType(OperationType.valueOf(operation));
        req.setStartDate(LocalDateTime.parse(start, dtf));
        req.setEndDate(LocalDateTime.parse(end, dtf));
        historyResults = calculatorService.getCalculationHistory(req);
    }
}