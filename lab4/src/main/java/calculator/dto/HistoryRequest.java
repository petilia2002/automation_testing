package calculator.dto;

import calculator.Calculator;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;

import java.time.LocalDateTime;

public class HistoryRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private OperationType operationType;
    private NumberSystem firstNumberSystem;
    private NumberSystem secondNumberSystem;

    // Конструкторы
    public HistoryRequest() {
    }

    public HistoryRequest(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public HistoryRequest(LocalDateTime startDate, LocalDateTime endDate,
                          OperationType operationType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.operationType = operationType;
    }

    public HistoryRequest(LocalDateTime startDate, LocalDateTime endDate,
                          OperationType operationType,
                          NumberSystem firstNumberSystem,
                          NumberSystem secondNumberSystem) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.operationType = operationType;
        this.firstNumberSystem = firstNumberSystem;
        this.secondNumberSystem = secondNumberSystem;
    }

    // Геттеры и сеттеры
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public NumberSystem getFirstNumberSystem() {
        return firstNumberSystem;
    }

    public void setFirstNumberSystem(NumberSystem firstNumberSystem) {
        this.firstNumberSystem = firstNumberSystem;
    }

    public NumberSystem getSecondNumberSystem() {
        return secondNumberSystem;
    }

    public void setSecondNumberSystem(NumberSystem secondNumberSystem) {
        this.secondNumberSystem = secondNumberSystem;
    }
}