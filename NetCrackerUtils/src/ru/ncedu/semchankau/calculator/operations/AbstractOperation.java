package ru.ncedu.semchankau.calculator.operations;

/**
 * Created by Aliaksei Semchankau on 11.07.2015.
 */
public abstract class AbstractOperation {

    /**
     * This method perfoms an operation with 2 doubles;
     * @param operand1
     * @param operand2
     * @return
     * @throws IllegalArgumentException
     */
    public abstract double operation (double operand1, double operand2)
            throws IllegalArgumentException ;

}
