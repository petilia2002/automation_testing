// src/test/java/calculator/config/CucumberSpringConfiguration.java
package calculator.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration extends AbstractIntegrationTest {
    // Этот класс предоставляет контекст Spring для Cucumber
}