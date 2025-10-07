package calculator.integration;

import calculator.repository.CalculationRepository;
import calculator.dto.CalculationRequest;
import calculator.dto.CalculationResponse;
import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculatorIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Загружаем тестовые данные перед каждым тестом
        testDataLoader.loadTestData();
    }

    @Test
    void testCalculateAndSave() throws IOException {
        // Берём первый тест из массива для запроса
        CalculationRequest request = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/calculations/calculations.json"),
                CalculationRequest[].class
        )[0];

        // Выполняем запрос
        ResponseEntity<CalculationResponse> response = restTemplate.postForEntity(
                "/api/calculator/calculate",
                request,
                CalculationResponse.class
        );

        // Проверяем результат
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        CalculationResponse expected = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/expected/calculation_response.json"),
                CalculationResponse.class
        );

        assertEquals(expected.getResult(), response.getBody().getResult());
        assertEquals(expected.getCalculationId(), response.getBody().getCalculationId());

        assertNotNull(response.getBody().getCalculationId());
    }

    @Test
    void testCalculateAndSave_AllOperations() throws IOException {
        // Читаем тестовые случаи
        CalculationTestCase[] testCases = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/calculations/calculation.json"),
                CalculationTestCase[].class
        );

        for (CalculationTestCase testCase : testCases) {
            System.out.println("Testing: " + testCase.getTestName());

            // Выполняем запрос
            ResponseEntity<CalculationResponse> response = restTemplate.postForEntity(
                    "/api/calculator/calculate",
                    testCase.getRequest(),
                    CalculationResponse.class
            );

            // Проверяем результат
            assertEquals(HttpStatus.OK, response.getStatusCode(),
                    "Test '" + testCase.getTestName() + "' should return OK status");
            assertNotNull(response.getBody(),
                    "Test '" + testCase.getTestName() + "' should return response body");

            assertEquals(testCase.getExpectedResult(), response.getBody().getResult(),
                    "Test '" + testCase.getTestName() + "' result mismatch");
            assertNotNull(response.getBody().getCalculationId(),
                    "Test '" + testCase.getTestName() + "' should return calculation ID");

            // Убедимся, что данные сохранились в БД
            Calculation savedCalculation = calculationRepository.findById(response.getBody().getCalculationId())
                    .orElse(null);
            assertNotNull(savedCalculation, "Calculation should be saved in database");
            assertEquals(testCase.getRequest().getFirstNumber(), savedCalculation.getFirstNumber());
            assertEquals(testCase.getRequest().getOperationType(), savedCalculation.getOperationType());

            System.out.println("✓ " + testCase.getTestName() + " - PASSED");
        }
    }

    @Test
    void testGetHistory() throws IOException {
        // Выполняем запрос на получение истории
        ResponseEntity<Calculation[]> response = restTemplate.getForEntity(
                "/api/calculator/history",
                Calculation[].class
        );

        // Проверяем результат
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Читаем ожидаемый результат из JSON
        List<Calculation> expected = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/expected/history_result.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Calculation.class)
        );

        assertEquals(expected.size(), response.getBody().length);

        // Сравниваем все записи по порядку
        for (int i = 0; i < expected.size(); i++) {
            Calculation actual = response.getBody()[i];
            Calculation exp = expected.get(i);

            assertEquals(exp.getFirstNumber(), actual.getFirstNumber(), "firstNumber mismatch at index " + i);
            assertEquals(exp.getSecondNumber(), actual.getSecondNumber(), "secondNumber mismatch at index " + i);
            assertEquals(exp.getFirstNumberSystem(), actual.getFirstNumberSystem(), "firstNumberSystem mismatch at index " + i);
            assertEquals(exp.getSecondNumberSystem(), actual.getSecondNumberSystem(), "secondNumberSystem mismatch at index " + i);
            assertEquals(exp.getOperationType(), actual.getOperationType(), "operationType mismatch at index " + i);
            assertEquals(exp.getResult(), actual.getResult(), "result mismatch at index " + i);
            assertEquals(exp.getCalculationDate(), actual.getCalculationDate(), "calculationDate mismatch at index " + i);
        }
    }

    @Test
    void testGetHistoryWithFilters() {
        // Тестируем фильтрацию
        ResponseEntity<Calculation[]> response = restTemplate.getForEntity(
                "/api/calculator/history?operationType=ADD&firstNumberSystem=BINARY",
                Calculation[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Проверяем что все результаты соответствуют фильтрам
        for (Calculation calc : response.getBody()) {
            assertEquals(OperationType.ADD, calc.getOperationType());
            assertEquals(NumberSystem.BINARY, calc.getFirstNumberSystem());
        }
    }

    @Test
    void testCreateNewCalculation() throws IOException {
        // Читаем тестовые данные из JSON
        CalculationRequest request = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/calculations/calculations.json"),
                CalculationRequest[].class
        )[0];

        // Создаем новое вычисление
        ResponseEntity<CalculationResponse> response = restTemplate.postForEntity(
                "/api/calculator/calculate",
                request,
                CalculationResponse.class
        );

        // Проверяем успешное создание
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("10000", response.getBody().getResult());
    }
}