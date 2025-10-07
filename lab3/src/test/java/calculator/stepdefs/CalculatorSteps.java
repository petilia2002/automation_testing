// src/test/java/calculator/stepdefs/CalculatorSteps.java
package calculator.stepdefs;

import calculator.dto.CalculationResponse;
import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import calculator.service.CalculatorService;
import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import calculator.dto.CalculationRequest;
import calculator.model.CalculationData;
import calculator.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorSteps {

    private final CalculatorService calculatorService;
    private final CalculationRepository calculationRepository;

    private List<CalculationResponse> createdResponses = new ArrayList<>();
    private List<CalculationData> inputData = new ArrayList<>();

    private String result;
    private List<Calculation> history;
    private String[] savedNumbers; // Для хранения чисел с кастомным разделителем

    @Autowired
    public CalculatorSteps(CalculatorService calculatorService,
                           CalculationRepository calculationRepository) {
        this.calculatorService = calculatorService;
        this.calculationRepository = calculationRepository;
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
}