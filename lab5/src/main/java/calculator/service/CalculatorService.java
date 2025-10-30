package calculator.service;

import calculator.Calculator;
import calculator.dto.CalculationRequest;
import calculator.dto.CalculationResponse;
import calculator.dto.HistoryRequest;
import calculator.entity.Calculation;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import calculator.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CalculatorService {

    @Autowired
    private CalculationRepository calculationRepository;

    private final Calculator calculator = new Calculator();

    /**
     * Сохраняет вычисление в базу данных
     * Работает с разными системами счисления для каждого числа
     */
    public Calculation saveCalculation(CalculationRequest request) {
        // Выполняем вычисление с учетом разных систем счисления
        String result = performCalculationWithDifferentSystems(request);

        // Создаем и сохраняем сущность
        Calculation calculation = new Calculation(
                request.getFirstNumber(),
                request.getFirstNumberSystem(),
                request.getSecondNumber(),
                request.getSecondNumberSystem(),
                request.getOperationType(),
                result,
                LocalDateTime.now()
        );

        return calculationRepository.save(calculation);
    }

    /**
     * Выполняет математическую операцию с числами в разных системах счисления
     */
    private String performCalculationWithDifferentSystems(CalculationRequest request) {
        try {
            // Конвертируем оба числа в десятичную систему для вычислений
            int num1 = convertToDecimal(request.getFirstNumber(), request.getFirstNumberSystem());
            int num2 = convertToDecimal(request.getSecondNumber(), request.getSecondNumberSystem());

            // Выполняем операцию
            int decimalResult;
            switch (request.getOperationType()) {
                case ADD:
                    decimalResult = num1 + num2;
                    break;
                case SUBTRACT:
                    decimalResult = num1 - num2;
                    break;
                case MULTIPLY:
                    decimalResult = num1 * num2;
                    break;
                case DIVIDE:
                    if (num2 == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    decimalResult = num1 / num2;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation type: " + request.getOperationType());
            }

            // Конвертируем результат обратно в систему счисления первого числа
            return convertFromDecimal(decimalResult, request.getFirstNumberSystem());

        } catch (Exception e) {
            throw new RuntimeException("Calculation error: " + e.getMessage(), e);
        }
    }

    /**
     * Конвертирует число из любой системы счисления в десятичную
     */
    private int convertToDecimal(String number, NumberSystem system) {
        try {
            switch (system) {
                case BINARY:
                    return Integer.parseInt(number, 2);
                case OCTAL:
                    return Integer.parseInt(number, 8);
                case DECIMAL:
                    return Integer.parseInt(number, 10);
                case HEXADECIMAL:
                    return Integer.parseInt(number, 16);
                default:
                    throw new IllegalArgumentException("Unsupported number system: " + system);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for " + system + ": " + number);
        }
    }

    /**
     * Конвертирует число из десятичной системы в указанную систему счисления
     */
    private String convertFromDecimal(int number, NumberSystem system) {
        switch (system) {
            case BINARY:
                return Integer.toBinaryString(number).toUpperCase();
            case OCTAL:
                return Integer.toOctalString(number).toUpperCase();
            case DECIMAL:
                return Integer.toString(number).toUpperCase();
            case HEXADECIMAL:
                return Integer.toHexString(number).toUpperCase();
            default:
                throw new IllegalArgumentException("Unsupported number system: " + system);
        }
    }

    /**
     * Сохраняет вычисление и возвращает Response DTO
     */
    public CalculationResponse saveCalculationWithResponse(CalculationRequest request) {
        Calculation savedCalculation = saveCalculation(request);
        return new CalculationResponse(savedCalculation.getResult(), savedCalculation.getId());
    }

    /**
     * Получение истории вычислений с фильтрацией
     */
    public List<Calculation> getCalculationHistory(HistoryRequest request) {
        // Если даты не указаны - возвращаем все записи
        if (request.getStartDate() == null && request.getEndDate() == null) {
            return calculationRepository.findAllWithOptionalFilters(
                    request.getOperationType(),
                    request.getFirstNumberSystem(),
                    request.getSecondNumberSystem()
            );
        }

        // Если указана только одна дата - ошибка
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Both startDate and endDate must be provided, or both omitted");
        }

        // Валидация диапазона дат
        validateDateRange(request);

        // Возвращаем записи за указанный период
        return calculationRepository.findByDateRangeWithOptionalFilters(
                request.getStartDate(),
                request.getEndDate(),
                request.getOperationType(),
                request.getFirstNumberSystem(),
                request.getSecondNumberSystem()
        );
    }

    /**
     * Валидация диапазона дат
     */
    private void validateDateRange(HistoryRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    // Старые методы для обратной совместимости
    public Calculation saveCalculation(String firstNumber, NumberSystem firstNumberSystem,
                                       String secondNumber, NumberSystem secondNumberSystem,
                                       OperationType operationType, String result) {
        Calculation calculation = new Calculation(
                firstNumber, firstNumberSystem,
                secondNumber, secondNumberSystem,
                operationType, result,
                LocalDateTime.now()
        );
        return calculationRepository.save(calculation);
    }
}