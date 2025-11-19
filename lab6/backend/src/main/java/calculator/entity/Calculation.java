package calculator.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_number", nullable = false)
    private String firstNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "first_number_system", nullable = false)
    private NumberSystem firstNumberSystem;

    @Column(name = "second_number", nullable = false)
    private String secondNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "second_number_system", nullable = false)
    private NumberSystem secondNumberSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "calculation_date", nullable = false)
    private LocalDateTime calculationDate;

    // Конструкторы
    public Calculation() {}

    public Calculation(String firstNumber, NumberSystem firstNumberSystem,
                       String secondNumber, NumberSystem secondNumberSystem,
                       OperationType operationType, String result,
                       LocalDateTime calculationDate) {
        this.firstNumber = firstNumber;
        this.firstNumberSystem = firstNumberSystem;
        this.secondNumber = secondNumber;
        this.secondNumberSystem = secondNumberSystem;
        this.operationType = operationType;
        this.result = result;
        this.calculationDate = calculationDate;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstNumber() { return firstNumber; }
    public void setFirstNumber(String firstNumber) { this.firstNumber = firstNumber; }

    public NumberSystem getFirstNumberSystem() { return firstNumberSystem; }
    public void setFirstNumberSystem(NumberSystem firstNumberSystem) { this.firstNumberSystem = firstNumberSystem; }

    public String getSecondNumber() { return secondNumber; }
    public void setSecondNumber(String secondNumber) { this.secondNumber = secondNumber; }

    public NumberSystem getSecondNumberSystem() { return secondNumberSystem; }
    public void setSecondNumberSystem(NumberSystem secondNumberSystem) { this.secondNumberSystem = secondNumberSystem; }

    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public LocalDateTime getCalculationDate() { return calculationDate; }
    public void setCalculationDate(LocalDateTime calculationDate) { this.calculationDate = calculationDate; }
}