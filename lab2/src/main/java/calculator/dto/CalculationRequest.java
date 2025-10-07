package calculator.dto;

import calculator.Calculator;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;

public class CalculationRequest {
    private String firstNumber;
    private NumberSystem firstNumberSystem;
    private String secondNumber;
    private NumberSystem secondNumberSystem;
    private OperationType operationType;

    // Конструкторы
    public CalculationRequest() {
    }

    public CalculationRequest(String firstNumber, NumberSystem firstNumberSystem,
                              String secondNumber, NumberSystem secondNumberSystem,
                              OperationType operationType) {
        this.firstNumber = firstNumber;
        this.firstNumberSystem = firstNumberSystem;
        this.secondNumber = secondNumber;
        this.secondNumberSystem = secondNumberSystem;
        this.operationType = operationType;
    }

    // Геттеры и сеттеры
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
}