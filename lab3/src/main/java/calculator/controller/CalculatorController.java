package calculator.controller;

import calculator.dto.CalculationRequest;
import calculator.dto.CalculationResponse;
import calculator.dto.HistoryRequest;
import calculator.entity.Calculation;
import calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    /**
     * Получение результата вычисления двух чисел (с разными системами счисления)
     * Все входные данные сохраняются в БД
     */
    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculateAndSave(@RequestBody CalculationRequest request) {
        try {
            CalculationResponse response = calculatorService.saveCalculationWithResponse(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CalculationResponse("Error: " + e.getMessage(), null)
            );
        }
    }

    /**
     * Получение данных о всех вычислениях за определенный период времени
     * С учетом операции и используемой системы счисления
     */
    @GetMapping("/history")
    public ResponseEntity<List<Calculation>> getHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String firstNumberSystem,
            @RequestParam(required = false) String secondNumberSystem) {

        try {
            HistoryRequest request = new HistoryRequest();
            request.setStartDate(startDate);
            request.setEndDate(endDate);

            if (operationType != null) {
                request.setOperationType(calculator.entity.OperationType.valueOf(operationType));
            }
            if (firstNumberSystem != null) {
                request.setFirstNumberSystem(calculator.entity.NumberSystem.valueOf(firstNumberSystem));
            }
            if (secondNumberSystem != null) {
                request.setSecondNumberSystem(calculator.entity.NumberSystem.valueOf(secondNumberSystem));
            }

            List<Calculation> result = calculatorService.getCalculationHistory(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Дополнительный endpoint для полной информации (можно оставить)
    @PostMapping("/calculate-full")
    public ResponseEntity<Calculation> calculateAndSaveFull(@RequestBody CalculationRequest request) {
        try {
            Calculation calculation = calculatorService.saveCalculation(request);
            return ResponseEntity.ok(calculation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}