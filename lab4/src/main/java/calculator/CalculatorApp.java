package calculator;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CalculatorApp {
    private static final Calculator calculator = new Calculator();
    private static final Scanner scanner = new Scanner(System.in);

    private static final Pattern BIN = Pattern.compile("^[+-]?[01]+$");
    private static final Pattern OCT = Pattern.compile("^[+-]?[0-7]+$");
    private static final Pattern DEC = Pattern.compile("^[+-]?\\d+$");
    private static final Pattern HEX = Pattern.compile("^[+-]?[0-9A-F]+$");

    public static void main(String[] args) {
        System.out.println("=== Калькулятор с поддержкой 4 систем счисления ===");

        while (true) {
            printMenu();
            int choice = getIntInRange("Выберите операцию: ", 0, 4);

            if (choice == 0) {
                System.out.println("Выход из программы...");
                break;
            }

            Calculator.NumberSystem system = selectNumberSystem();
            String a = getNumberInput("Введите первое число: ", system);
            String b = getNumberInput("Введите второе число: ", system);

            if (choice == 4 && isZero(b, system)) {
                System.out.println("Ошибка: деление на ноль запрещено. Введите другое значение делителя.");
                System.out.println();
                continue;
            }

            try {
                String result = performOperation(choice, a, b, system);
                System.out.println("Результат: " + result);
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: число вне диапазона 32-битного целого (int). Попробуйте меньшее по модулю число.");
            } catch (ArithmeticException ex) {
                System.out.println("Ошибка арифметики: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                System.out.println("Ошибка: " + ex.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("1. Сложение");
        System.out.println("2. Вычитание");
        System.out.println("3. Умножение");
        System.out.println("4. Деление");
        System.out.println("0. Выход");
    }

    private static Calculator.NumberSystem selectNumberSystem() {
        System.out.println("\nВыберите систему счисления:");
        System.out.println("1. Двоичная (BINARY)");
        System.out.println("2. Восьмеричная (OCTAL)");
        System.out.println("3. Десятичная (DECIMAL)");
        System.out.println("4. Шестнадцатеричная (HEXADECIMAL)");

        int choice = getIntInRange("Ваш выбор: ", 1, 4);
        switch (choice) {
            case 1: return Calculator.NumberSystem.BINARY;
            case 2: return Calculator.NumberSystem.OCTAL;
            case 3: return Calculator.NumberSystem.DECIMAL;
            case 4: return Calculator.NumberSystem.HEXADECIMAL;
            default: throw new IllegalStateException("Неожиданное значение выбора: " + choice);
        }
    }

    private static String performOperation(int choice, String a, String b,
                                           Calculator.NumberSystem system) {
        switch (choice) {
            case 1: return calculator.add(a, b, system);
            case 2: return calculator.subtract(a, b, system);
            case 3: return calculator.multiply(a, b, system);
            case 4: return calculator.divide(a, b, system);
            default: throw new IllegalArgumentException("Неизвестная операция");
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                System.out.println("Нет ввода. Повторите попытку.");
                continue;
            }
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.printf("Введите число в диапазоне [%d..%d].%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число (например: 0, 1, 2...).");
            }
        }
    }

    private static String getNumberInput(String prompt, Calculator.NumberSystem system) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            String s = raw.toUpperCase(Locale.ROOT);

            if (s.isEmpty()) {
                System.out.println("Пустой ввод. Повторите попытку.");
                continue;
            }
            if (!isValidForSystem(s, system)) {
                System.out.printf(
                        "Неверный формат для %s. %s%n",
                        system, allowedHint(system)
                );
                continue;
            }
            try {
                Integer.parseInt(s, system.getBase());
            } catch (NumberFormatException ex) {
                System.out.println("Число вне диапазона 32-битного int. Введите меньшее по модулю число.");
                continue;
            }
            return s;
        }
    }

    private static boolean isValidForSystem(String s, Calculator.NumberSystem system) {
        switch (system) {
            case BINARY:       return BIN.matcher(s).matches();
            case OCTAL:        return OCT.matcher(s).matches();
            case DECIMAL:      return DEC.matcher(s).matches();
            case HEXADECIMAL:  return HEX.matcher(s).matches();
            default:           return false;
        }
    }

    private static String allowedHint(Calculator.NumberSystem system) {
        switch (system) {
            case BINARY:       return "Допустимы только 0 и 1";
            case OCTAL:        return "Допустимы цифры 0–7";
            case DECIMAL:      return "Допустимы цифры 0–9";
            case HEXADECIMAL:  return "Допустимы 0–9 и A–F";
            default:           return "";
        }
    }

    private static boolean isZero(String s, Calculator.NumberSystem system) {
        try {
            int v = Integer.parseInt(s, system.getBase());
            return v == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
