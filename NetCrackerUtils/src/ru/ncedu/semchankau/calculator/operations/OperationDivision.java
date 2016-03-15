package ru.ncedu.semchankau.calculator.operations;

/**
 * Created by Aliaksei Semchankau on 11.07.2015.
 */
public class OperationDivision extends AbstractOperation{

    @Override
    public double operation(double operand1, double operand2){
        if (Math.abs(operand2) < 1e-8) {
            System.out.println("division by zero");
            throw new IllegalArgumentException();
        }
        try {
            return operand1 / operand2;
        } catch (IllegalArgumentException iaExc) {
            System.out.println("division by zero");
            throw new IllegalArgumentException();
        }
    }

}
