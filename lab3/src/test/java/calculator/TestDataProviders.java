package calculator;

import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestDataProviders {

    public static Stream<Arguments> additionTestData() {
        return Stream.of(
                arguments("1010", "110", Calculator.NumberSystem.BINARY, "10000"),
                arguments("17", "5", Calculator.NumberSystem.OCTAL, "24"),
                arguments("15", "7", Calculator.NumberSystem.DECIMAL, "22"),
                arguments("A", "5", Calculator.NumberSystem.HEXADECIMAL, "F")
        );
    }

    public static Stream<Arguments> subtractionTestData() {
        return Stream.of(
                arguments("1010", "110", Calculator.NumberSystem.BINARY, "100"),
                arguments("17", "5", Calculator.NumberSystem.OCTAL, "12"),
                arguments("15", "7", Calculator.NumberSystem.DECIMAL, "8"),
                arguments("A", "5", Calculator.NumberSystem.HEXADECIMAL, "5")
        );
    }

    public static Stream<Arguments> multiplicationTestData() {
        return Stream.of(
                arguments("1010", "11", Calculator.NumberSystem.BINARY, "11110"),
                arguments("7", "5", Calculator.NumberSystem.OCTAL, "43"),
                arguments("15", "3", Calculator.NumberSystem.DECIMAL, "45"),
                arguments("A", "5", Calculator.NumberSystem.HEXADECIMAL, "32")
        );
    }

    public static Stream<Arguments> divisionTestData() {
        return Stream.of(
                arguments("1010", "10", Calculator.NumberSystem.BINARY, "101"),
                arguments("24", "3", Calculator.NumberSystem.OCTAL, "6"),
                arguments("15", "3", Calculator.NumberSystem.DECIMAL, "5"),
                arguments("1E", "3", Calculator.NumberSystem.HEXADECIMAL, "A")
        );
    }
}