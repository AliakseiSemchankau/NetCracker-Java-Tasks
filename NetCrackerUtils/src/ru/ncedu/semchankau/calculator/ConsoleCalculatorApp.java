package ru.ncedu.semchankau.calculator;

import ru.ncedu.semchankau.calculator.operations.PossibleOperations;

/**
 * Created by Aliaksei Semchankau on 11.07.2015.
 */
public class ConsoleCalculatorApp {

    /**
     * here we are listing all functions that we would use in calculator
     * and put them as constructor parameter;
     * @param argc
     */
    public static void main(final String... argc) {

        PossibleOperations possibles = new PossibleOperations();
        possibles.addOperation("+", "OperationPlus");
        possibles.addOperation("-", "OperationMinus");
        possibles.addOperation("*", "OperationMultiply");
        possibles.addOperation("/", "OperationDivision");
        ConsoleCalculator calculator = new ConsoleCalculator(possibles);
        while (true) {
            System.out.println(calculator.makeOperation());
        }

    }

}
