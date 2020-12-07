package Parser;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class ArithmeticExpression and the main method
 */

import Parser.Expression.ArithmeticExpression;

public class LitteralInteger extends ArithmeticExpression {

    private int valueInteger;

    public LitteralInteger(int valueInteger) {
        this.valueInteger = valueInteger;
    }

    @Override
    public String toString() {
        return ""+valueInteger;
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = indent+"Literal Integer: "+Integer.toString(valueInteger);
        return s;
    }

    @Override
    public int execute() throws ExecuterException {
        return this.valueInteger;
    }
}
