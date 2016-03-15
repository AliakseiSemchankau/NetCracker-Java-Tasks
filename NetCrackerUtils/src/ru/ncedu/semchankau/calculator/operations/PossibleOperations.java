package ru.ncedu.semchankau.calculator.operations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aliaksei Semchankau on 23.08.2015.
 */
public class PossibleOperations {

    private static List<String> symbolOperations = new ArrayList<String>();
    private static List<String> possibleOperations = new ArrayList<String>();


    public static void addOperation(String symbol, String operation) {
        symbolOperations.add(symbol);
        possibleOperations.add(operation);
    }

    public static List<String> getOperations() {
        return possibleOperations;
    }

    public static List<String> getSymbols() {
        return symbolOperations;
    }

}
