package calculator.integration;

import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import calculator.repository.CalculationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TestDataLoader {

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void loadTestData() {
        calculationRepository.deleteAll();
        loadCalculationsFromFile("classpath:test_data/calculations/calculations.json");
    }

    private void loadCalculationsFromFile(String filePath) {
        try {
            Calculation[] calculations = objectMapper.readValue(
                    new PathMatchingResourcePatternResolver().getResource(filePath).getInputStream(),
                    Calculation[].class
            );

            for (Calculation calc : calculations) {
                calculationRepository.save(calc);
            }

            System.out.println("Loaded " + calculations.length + " calculations from " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data from " + filePath, e);
        }
    }

    /**
     * Альтернативный способ - создание данных через код
     */
    public void createTestDataProgrammatically() {
        List<Calculation> testCalculations = Arrays.asList(
                new Calculation("1010", NumberSystem.BINARY, "110", NumberSystem.BINARY,
                        OperationType.ADD, "10000", LocalDateTime.now()),
                new Calculation("17", NumberSystem.OCTAL, "5", NumberSystem.OCTAL,
                        OperationType.SUBTRACT, "12", LocalDateTime.now()),
                new Calculation("A", NumberSystem.HEXADECIMAL, "5", NumberSystem.HEXADECIMAL,
                        OperationType.MULTIPLY, "32", LocalDateTime.now().minusHours(1))
        );

        calculationRepository.saveAll(testCalculations);
    }
}