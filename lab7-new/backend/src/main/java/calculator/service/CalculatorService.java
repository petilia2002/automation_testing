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

// Добавьте эти импорты в начало файла
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

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

    private static final int SCALE = 20; // точность для операций деления (кол-во знаков после запятой)
    private static final int MAX_FRACTION_DIGITS = 30; // макс. цифр при конвертации дробной части в другие СС

    private String performCalculationWithDifferentSystems(CalculationRequest request) {
        try {
            // Конвертируем оба числа в десятичную систему как BigDecimal
            BigDecimal num1 = convertToDecimal(request.getFirstNumber(), request.getFirstNumberSystem());
            BigDecimal num2 = convertToDecimal(request.getSecondNumber(), request.getSecondNumberSystem());

            // Выполняем операцию
            BigDecimal decimalResult;
            switch (request.getOperationType()) {
                case ADD:
                    decimalResult = num1.add(num2);
                    break;
                case SUBTRACT:
                    decimalResult = num1.subtract(num2);
                    break;
                case MULTIPLY:
                    decimalResult = num1.multiply(num2);
                    break;
                case DIVIDE:
                    if (num2.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    // задаём точность деления
                    decimalResult = num1.divide(num2, SCALE, RoundingMode.HALF_UP);
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
     * Конвертирует число из любой системы счисления в десятичную (BigDecimal)
     */
    private BigDecimal convertToDecimal(String number, NumberSystem system) {
        try {
            switch (system) {
                case BINARY:
                    return new BigDecimal(new BigInteger(number, 2));
                case OCTAL:
                    return new BigDecimal(new BigInteger(number, 8));
                case DECIMAL:
                    // допускаем целые и дробные десятичные строки
                    return new BigDecimal(number);
                case HEXADECIMAL:
                    return new BigDecimal(new BigInteger(number, 16));
                default:
                    throw new IllegalArgumentException("Unsupported number system: " + system);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for " + system + ": " + number);
        }
    }

    /**
     * Конвертирует число из десятичной BigDecimal в указанную систему счисления.
     * Поддерживает дробную часть (до MAX_FRACTION_DIGITS).
     */
    private String convertFromDecimal(BigDecimal number, NumberSystem system) {
        // Обрабатываем знак
        boolean negative = number.signum() < 0;
        BigDecimal absNumber = number.abs();

        switch (system) {
            case DECIMAL:
                // Убираем лишние нули
                String dec = absNumber.stripTrailingZeros().toPlainString();
                return negative ? "-" + dec : dec;

            case BINARY:
                return (negative ? "-" : "") + convertToBase(absNumber, 2);
            case OCTAL:
                return (negative ? "-" : "") + convertToBase(absNumber, 8);
            case HEXADECIMAL:
                return (negative ? "-" : "") + convertToBase(absNumber, 16).toUpperCase();
            default:
                throw new IllegalArgumentException("Unsupported number system: " + system);
        }
    }

    /**
     * Конвертирует положительное BigDecimal в позиционную систему с основанием radix (2,8,16 и т.д.)
     * Возвращает строку вида "INT.FRACTION" (если есть дробная часть).
     */
    private String convertToBase(BigDecimal absNumber, int radix) {
        BigInteger intPart = absNumber.toBigInteger(); // целая часть
        BigDecimal fraction = absNumber.subtract(new BigDecimal(intPart)); // дробная часть

        // преобразуем целую часть
        String intStr = intPart.equals(BigInteger.ZERO) ? "0" : intPart.toString(radix);

        if (fraction.compareTo(BigDecimal.ZERO) == 0) {
            return intStr;
        }

        // преобразуем дробную часть: умножаем на radix и берём целую часть итеративно
        StringBuilder fracBuilder = new StringBuilder();
        BigDecimal current = fraction;
        for (int i = 0; i < MAX_FRACTION_DIGITS && current.compareTo(BigDecimal.ZERO) != 0; i++) {
            current = current.multiply(BigDecimal.valueOf(radix));
            BigInteger digitBI = current.toBigInteger();
            int digit = digitBI.intValue();
            fracBuilder.append(digitToChar(digit));
            current = current.subtract(new BigDecimal(digitBI));
        }

        return intStr + "." + fracBuilder.toString();
    }

    private char digitToChar(int digit) {
        if (digit >= 0 && digit <= 9) return (char) ('0' + digit);
        return (char) ('A' + (digit - 10)); // для hex и т.п.
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