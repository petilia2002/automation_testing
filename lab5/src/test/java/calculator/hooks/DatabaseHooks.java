// src/test/java/calculator/hooks/DatabaseHooks.java
package calculator.hooks;

import io.cucumber.java.*;
import calculator.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DatabaseHooks {

    private final CalculationRepository calculationRepository;

    @Autowired
    public DatabaseHooks(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("Начало сценария: " + scenario.getName());
        calculationRepository.deleteAll();
    }

    @After
    public void afterScenario(Scenario scenario) {
        calculationRepository.deleteAll();
        System.out.println("Завершение сценария: " + scenario.getName() + " - Status: " + scenario.getStatus());
    }
}