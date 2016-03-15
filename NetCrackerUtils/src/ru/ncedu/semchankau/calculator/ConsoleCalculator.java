package ru.ncedu.semchankau.calculator;

import ru.ncedu.semchankau.calculator.operations.AbstractOperation;
import ru.ncedu.semchankau.calculator.operations.PossibleOperations;

import java.util.Scanner;
import java.util.TreeMap;


/**
 * Created by Aliaksei Semchankau on 11.07.2015.
 */
public class ConsoleCalculator {

    private TreeMap<String, AbstractOperation> operationsMap = new TreeMap<String, AbstractOperation>();

    /**
     * This constructor takes possible operations and adds them to map.
     * @param possibles
     */
    public ConsoleCalculator(final PossibleOperations possibles) {

        for (int i = 0; i < possibles.getSymbols().size(); ++i) {
            String symbol = possibles.getSymbols().get(i);
            String operation = possibles.getOperations().get(i);
            String classNameForCommand = "ru.ncedu.semchankau.calculator.operations." + operation;
            try {
                Object obj = Class.forName(classNameForCommand).newInstance();
                AbstractOperation abstractOperation = (AbstractOperation) obj;
                operationsMap.put(symbol, abstractOperation);
            } catch (ClassNotFoundException cnfExc) {
                System.out.println("class haven't been found" + operation);
            } catch (IllegalAccessException iaExc) {
                System.out.println("can't get access to classNameForCommand");
            } catch (InstantiationException iExc) {
                System.out.println("can't instantiate class " + classNameForCommand);
            }
        }


    }

    /**
     * when request is coming, then we take an operation
     * and watching for its realisation among AbstractOperation
     * functions;
     * @return
     * @throws IllegalArgumentException
     */
    public double makeOperation() throws IllegalArgumentException {

        Scanner scan = new Scanner(System.in);

        double firstOperand;
        String operation;
        double secondOperand;

        System.out.println("first operand:");

        if (scan.hasNextDouble()) {
            firstOperand = scan.nextDouble();
        } else {
            System.out.println("first operand is incorrect");
            throw new IllegalArgumentException();
        }

        System.out.println("operation:");
        operation = scan.next();

        System.out.println("second operand:");

        if (scan.hasNextDouble()) {
            secondOperand = scan.nextDouble();
        } else {
            System.out.println("second operand is incorrect");
            throw new IllegalArgumentException();
        }

        if (!operationsMap.containsKey(operation)) {
            System.out.println("there is no such operation: " + operation);
            throw new IllegalArgumentException();
        }

        try {
            return operationsMap.get(operation).operation(firstOperand, secondOperand);
        } catch (IllegalArgumentException iaExc) {
            System.out.println("incorrect operands for operation " + operation);
            throw new IllegalArgumentException();
        }
    }

}
