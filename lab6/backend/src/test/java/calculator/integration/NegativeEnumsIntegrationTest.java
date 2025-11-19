package calculator.integration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NegativeEnumsIntegrationTest {

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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testDataLoader.loadTestData();
    }

    // --- POST /api/calculator/calculate ---

    @Test
    void calculate_shouldReturn400_onInvalidOperationType() throws Exception {
        Map[] cases = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/negative/calculate_invalid_operationType.json"),
                Map[].class
        );
        for (Map<String, Object> payload : cases) {
            ResponseEntity<String> response = postCalculate(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void calculate_shouldReturn400_onInvalidNumberSystem() throws Exception {
        Map[] cases = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/negative/calculate_invalid_numberSystem.json"),
                Map[].class
        );
        for (Map<String, Object> payload : cases) {
            ResponseEntity<String> response = postCalculate(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void calculate_shouldReturn400_onLowercaseEnums() throws Exception {
        Map[] cases = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/negative/calculate_lowercase_enums.json"),
                Map[].class
        );
        for (Map<String, Object> payload : cases) {
            ResponseEntity<String> response = postCalculate(payload);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    private ResponseEntity<String> postCalculate(Map<String, Object> payload) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = objectMapper.writeValueAsString(payload);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity("/api/calculator/calculate", entity, String.class);
    }

    // --- GET /api/calculator/history ---

    @Test
    void history_shouldReturn400_onInvalidOperationType() throws Exception {
        Map<?, ?> root = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/negative/history_invalid_enums.json"),
                Map.class
        );
        for (String op : (Iterable<String>) root.get("invalidOperationTypes")) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "/api/calculator/history?operationType=" + op, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void history_shouldReturn400_onInvalidNumberSystems() throws Exception {
        Map<?, ?> root = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/negative/history_invalid_enums.json"),
                Map.class
        );
        for (String sys : (Iterable<String>) root.get("invalidFirstSystems")) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "/api/calculator/history?firstNumberSystem=" + sys, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
        for (String sys : (Iterable<String>) root.get("invalidSecondSystems")) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "/api/calculator/history?secondNumberSystem=" + sys, String.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void history_shouldReturn400_onLowercaseEnums() throws Exception {
        Map<?, ?> root = objectMapper.readValue(
                getClass().getResourceAsStream("/test_data/negative/history_invalid_enums.json"),
                Map.class
        );
        ResponseEntity<String> r1 = restTemplate.getForEntity(
                "/api/calculator/history?operationType=" + ((Iterable<String>) root.get("invalidOperationTypes")).iterator().next(),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, r1.getStatusCode());
    }
}


