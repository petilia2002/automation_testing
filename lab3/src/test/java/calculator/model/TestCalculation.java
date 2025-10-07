// src/test/java/calculator/model/TestCalculation.java
package calculator.model;

public class TestCalculation {
    private String firstNumber;
    private String firstNumberSystem;
    private String secondNumber;
    private String secondNumberSystem;
    private String operationType;
    private String expectedResult;

    // Конструктор по умолчанию (обязателен для Cucumber)
    public TestCalculation() {
    }

    // Конструктор с параметрами
    public TestCalculation(String firstNumber, String firstNumberSystem,
                           String secondNumber, String secondNumberSystem,
                           String operationType, String expectedResult) {
        this.firstNumber = firstNumber;
        this.firstNumberSystem = firstNumberSystem;
        this.secondNumber = secondNumber;
        this.secondNumberSystem = secondNumberSystem;
        this.operationType = operationType;
        this.expectedResult = expectedResult;
    }

    // Геттеры и сеттеры (обязательны для Cucumber)
    public String getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(String firstNumber) {
        this.firstNumber = firstNumber;
    }

    public String getFirstNumberSystem() {
        return firstNumberSystem;
    }

    public void setFirstNumberSystem(String firstNumberSystem) {
        this.firstNumberSystem = firstNumberSystem;
    }

    public String getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(String secondNumber) {
        this.secondNumber = secondNumber;
    }

    public String getSecondNumberSystem() {
        return secondNumberSystem;
    }

    public void setSecondNumberSystem(String secondNumberSystem) {
        this.secondNumberSystem = secondNumberSystem;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }
}