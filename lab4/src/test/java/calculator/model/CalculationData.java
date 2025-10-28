package calculator.model;

import calculator.entity.NumberSystem;
import calculator.entity.OperationType;

public class CalculationData {
    private String firstNumber;
    private NumberSystem firstNumberSystem;
    private String secondNumber;
    private NumberSystem secondNumberSystem;
    private OperationType operationType;
    private String expectedResult;

    // Обязательно нужны геттеры и сеттеры!
    public String getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(String firstNumber) {
        this.firstNumber = firstNumber;
    }

    public NumberSystem getFirstNumberSystem() {
        return firstNumberSystem;
    }

    public void setFirstNumberSystem(NumberSystem firstNumberSystem) {
        this.firstNumberSystem = firstNumberSystem;
    }

    public String getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(String secondNumber) {
        this.secondNumber = secondNumber;
    }

    public NumberSystem getSecondNumberSystem() {
        return secondNumberSystem;
    }

    public void setSecondNumberSystem(NumberSystem secondNumberSystem) {
        this.secondNumberSystem = secondNumberSystem;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s => %s",
                firstNumber, firstNumberSystem, secondNumber, secondNumberSystem, operationType, expectedResult);
    }
}
