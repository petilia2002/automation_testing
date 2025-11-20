package calculator.config;

import calculator.model.CalculationData;
import calculator.entity.NumberSystem;
import calculator.entity.OperationType;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class CustomDataTableType {

    @DataTableType
    public CalculationData calculationDataEntry(Map<String, String> entry) {
        CalculationData data = new CalculationData();
        data.setFirstNumber(entry.get("firstNumber"));
        data.setFirstNumberSystem(NumberSystem.valueOf(entry.get("firstNumberSystem")));
        data.setSecondNumber(entry.get("secondNumber"));
        data.setSecondNumberSystem(NumberSystem.valueOf(entry.get("secondNumberSystem")));
        data.setOperationType(OperationType.valueOf(entry.get("operationType")));
        data.setExpectedResult(entry.get("expectedResult"));
        return data;
    }
}
