package calculator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import java.util.List;

import java.io.IOException;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Addition Tests")
    class AdditionTests {

        @ParameterizedTest(name = "[{index}] {0} + {1} в {2} системе = {3}")
        @CsvSource({
                "1010, 110, BINARY, 10000",
                "17, 5, OCTAL, 24",
                "15, 7, DECIMAL, 22",
                "A, 5, HEXADECIMAL, F"
        })
        @DisplayName("Addition with CSV source")
        void testAddition(String a, String b, Calculator.NumberSystem system, String expected) {
            String result = calculator.add(a, b, system);
            assertEquals(expected, result,
                    String.format("%s + %s in %s should equal %s", a, b, system, expected));
        }

        @ParameterizedTest(name = "[{index}] {0} + {1} в {2} системе = {3}")
        @CsvFileSource(resources = "/add_tests.csv", numLinesToSkip = 1)
        @DisplayName("Addition with CSV file source")
        void testAdditionFromCsvFile(String a, String b, String systemStr, String expected) {
            Calculator.NumberSystem system = Calculator.NumberSystem.valueOf(systemStr);
            String result = calculator.add(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "[{index}] {0} + {1} в {2} = {3}")
        @MethodSource("calculator.TestDataProviders#additionTestData")
        @DisplayName("Addition with External Method Source")
        void testAdditionWithExternalMethodSource(String a, String b,
                                                  Calculator.NumberSystem system,
                                                  String expected) {
            String result = calculator.add(a, b, system);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Subtraction Tests")
    class SubtractionTests {

        @ParameterizedTest(name = "[{index}] {0} - {1} в {2} системе = {3}")
        @CsvSource({
                "1010, 110, BINARY, 100",
                "17, 5, OCTAL, 12",
                "15, 7, DECIMAL, 8",
                "A, 5, HEXADECIMAL, 5"
        })
        @DisplayName("Subtraction with CSV source")
        void testSubtraction(String a, String b, Calculator.NumberSystem system, String expected) {
            String result = calculator.subtract(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "[{index}] {0} - {1} в {2} системе = {3}")
        @CsvFileSource(resources = "/sub_tests.csv", numLinesToSkip = 1)
        @DisplayName("Subtraction with CSV file source")
        void testSubtractionFromCsvFile(String a, String b, String systemStr, String expected) {
            Calculator.NumberSystem system = Calculator.NumberSystem.valueOf(systemStr);
            String result = calculator.subtract(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "[{index}] {0} - {1} в {2} = {3}")
        @MethodSource("calculator.TestDataProviders#subtractionTestData")
        @DisplayName("Subtraction with External Method Source")
        void testSubtractionWithExternalMethodSource(String a, String b,
                                                  Calculator.NumberSystem system,
                                                  String expected) {
            String result = calculator.subtract(a, b, system);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Multiplication Tests")
    class MultiplicationTests {

        @ParameterizedTest(name = "[{index}] {0} x {1} в {2} системе = {3}")
        @CsvSource({
                "1010, 11, BINARY, 11110",
                "7, 5, OCTAL, 43",
                "15, 3, DECIMAL, 45",
                "A, 5, HEXADECIMAL, 32"
        })
        @DisplayName("Multiplication with CSV source")
        void testMultiplication(String a, String b, Calculator.NumberSystem system, String expected) {
            String result = calculator.multiply(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "[{index}] {0} x {1} в {2} системе = {3}")
        @CsvFileSource(resources = "/mul_tests.csv", numLinesToSkip = 1)
        @DisplayName("Multiplication with CSV file source")
        void testMultiplicationFromCsvFile(String a, String b, String systemStr, String expected) {
            Calculator.NumberSystem system = Calculator.NumberSystem.valueOf(systemStr);
            String result = calculator.multiply(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "[{index}] {0} x {1} в {2} = {3}")
        @MethodSource("calculator.TestDataProviders#multiplicationTestData")
        @DisplayName("Multiplication with External Method Source")
        void testMultiplicationWithExternalMethodSource(String a, String b,
                                                     Calculator.NumberSystem system,
                                                     String expected) {
            String result = calculator.multiply(a, b, system);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Division Tests")
    class DivisionTests {

        @ParameterizedTest(name = "[{index}] {0} / {1} в {2} системе = {3}")
        @CsvSource({
                "1010, 10, BINARY, 101",
                "24, 3, OCTAL, 6",
                "15, 3, DECIMAL, 5",
                "1E, 3, HEXADECIMAL, A"
        })
        @DisplayName("Division with CSV source")
        void testDivision(String a, String b, Calculator.NumberSystem system, String expected) {
            String result = calculator.divide(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest
        @EnumSource(Calculator.NumberSystem.class)
        @DisplayName("Division by zero should throw exception")
        void testDivisionByZero(Calculator.NumberSystem system) {
            assertThrows(ArithmeticException.class, () -> {
                calculator.divide("10", "0", system);
            });
        }

        @ParameterizedTest(name = "[{index}] {0} / {1} в {2} системе = {3}")
        @CsvFileSource(resources = "/div_tests.csv", numLinesToSkip = 1)
        @DisplayName("Division with CSV file source")
        void testDivisionFromCsvFile(String a, String b, String systemStr, String expected) {
            Calculator.NumberSystem system = Calculator.NumberSystem.valueOf(systemStr);
            String result = calculator.divide(a, b, system);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "[{index}] {0} / {1} в {2} = {3}")
        @MethodSource("calculator.TestDataProviders#divisionTestData")
        @DisplayName("Division with External Method Source")
        void testDivisionWithExternalMethodSource(String a, String b,
                                                        Calculator.NumberSystem system,
                                                        String expected) {
            String result = calculator.divide(a, b, system);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("Dynamic Tests from CSV")
    class DynamicTests {
        @TestFactory
        @DisplayName("Dynamic tests for all operations")
        Stream<DynamicTest> dynamicTestsFromCsvFile() throws IOException {
            try (InputStream in = getClass().getResourceAsStream("/test_data.csv")) {
                assert in != null;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                    List<String[]> testData = reader.lines()
                            .skip(1)
                            .map(line -> line.split(","))
                            .collect(Collectors.toList());

                    return testData.stream()
                            .map(data -> DynamicTest.dynamicTest(
                                    String.format("%s: %s %s %s (в %s) = %s",
                                            data[0], data[1], getSymbol(data[0]), data[2], data[3], data[4]),
                                    () -> {
                                        Calculator.NumberSystem system = Calculator.NumberSystem.valueOf(data[3]);
                                        String result;
                                        switch (data[0]) {
                                            case "ADD": result = calculator.add(data[1], data[2], system); break;
                                            case "SUBTRACT": result = calculator.subtract(data[1], data[2], system); break;
                                            case "MULTIPLY": result = calculator.multiply(data[1], data[2], system); break;
                                            case "DIVIDE": result = calculator.divide(data[1], data[2], system); break;
                                            default: throw new IllegalArgumentException("Unknown operation: " + data[0]);
                                        }
                                        assertEquals(data[4], result);
                                    }
                            ));
                }
            }
        }

        private String getSymbol(String operation) {
            switch (operation) {
                case "ADD": return "+";
                case "SUBTRACT": return "-";
                case "MULTIPLY": return "*";
                case "DIVIDE": return "/";
                default: return "?";
            }
        }

    }
}