package calculator.dto;

public class CalculationResponse {
    private String result;
    private Long calculationId;

    // Конструкторы
    public CalculationResponse() {
    }

    public CalculationResponse(String result, Long calculationId) {
        this.result = result;
        this.calculationId = calculationId;
    }

    // Геттеры и сеттеры
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getCalculationId() {
        return calculationId;
    }

    public void setCalculationId(Long calculationId) {
        this.calculationId = calculationId;
    }
}