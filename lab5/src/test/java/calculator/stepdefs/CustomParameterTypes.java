package calculator.stepdefs;

import io.cucumber.java.ParameterType;

import java.util.Arrays;
import java.util.List;

public class CustomParameterTypes {

    @ParameterType(".*")
    public List<String> semicolonSeparatedList(String input) {
        if (input == null || input.trim().isEmpty()) {
            return List.of();
        }
        input = input.replace("\"", "").trim();
        return Arrays.stream(input.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
