package calculator;

public class Calculator {

    public enum NumberSystem {
        BINARY(2), OCTAL(8), DECIMAL(10), HEXADECIMAL(16);

        private final int base;

        NumberSystem(int base) {
            this.base = base;
        }

        public int getBase() {
            return base;
        }
    }

    public String add(String a, String b, NumberSystem system) {
        int num1 = Integer.parseInt(a, system.getBase());
        int num2 = Integer.parseInt(b, system.getBase());
        int result = num1 + num2;
        return Integer.toString(result, system.getBase()).toUpperCase();
    }

    public String subtract(String a, String b, NumberSystem system) {
        int num1 = Integer.parseInt(a, system.getBase());
        int num2 = Integer.parseInt(b, system.getBase());
        int result = num1 - num2;
        return Integer.toString(result, system.getBase()).toUpperCase();
    }

    public String multiply(String a, String b, NumberSystem system) {
        int num1 = Integer.parseInt(a, system.getBase());
        int num2 = Integer.parseInt(b, system.getBase());
        int result = num1 * num2;
        return Integer.toString(result, system.getBase()).toUpperCase();
    }

    public String divide(String a, String b, NumberSystem system) {
        int num1 = Integer.parseInt(a, system.getBase());
        int num2 = Integer.parseInt(b, system.getBase());

        if (num2 == 0) {
            throw new ArithmeticException("Division by zero");
        }

        int result = num1 / num2;
        return Integer.toString(result, system.getBase()).toUpperCase();
    }
}