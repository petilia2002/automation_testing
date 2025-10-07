package calculator.integration;

import calculator.dto.CalculationRequest;

public class CalculationTestCase {
    private String testName;
    private CalculationRequest request;
    private String expectedResult;

    // геттеры и сеттеры
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public CalculationRequest getRequest() { return request; }
    public void setRequest(CalculationRequest request) { this.request = request; }

    public String getExpectedResult() { return expectedResult; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }
}