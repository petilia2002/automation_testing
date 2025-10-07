// src/test/java/calculator/stepdefs/CalculatorSteps.java
package calculator.stepdefs;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import calculator.service.CalculatorService;
import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import calculator.dto.CalculationRequest;
import calculator.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorSteps {

    private final CalculatorService calculatorService;
    private final CalculationRepository calculationRepository;

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
}