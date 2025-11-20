package calculator.model;

import calculator.entity.NumberSystem;
import calculator.entity.OperationType;

import java.time.LocalDateTime;

public class HistoryFilter {
    private OperationType operationType;
    private NumberSystem firstNumberSystem;
    private NumberSystem secondNumberSystem;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer expectedCount;

    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }

    public NumberSystem getFirstNumberSystem() { return firstNumberSystem; }
    public void setFirstNumberSystem(NumberSystem firstNumberSystem) { this.firstNumberSystem = firstNumberSystem; }

    public NumberSystem getSecondNumberSystem() { return secondNumberSystem; }
    public void setSecondNumberSystem(NumberSystem secondNumberSystem) { this.secondNumberSystem = secondNumberSystem; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Integer getExpectedCount() { return expectedCount; }
    public void setExpectedCount(Integer expectedCount) { this.expectedCount = expectedCount; }

    @Override
    public String toString() {
        return "HistoryFilter{" +
                "operationType=" + operationType +
                ", firstNumberSystem=" + firstNumberSystem +
                ", secondNumberSystem=" + secondNumberSystem +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", expectedCount=" + expectedCount +
                '}';
    }
}
