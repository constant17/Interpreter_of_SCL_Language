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

public class LiteralString extends ArithmeticExpression {

    private String stringValue;

    public LiteralString(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return "String Literal: "+stringValue;
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = indent+"String Literal: "+stringValue;
        return s;
    }

    @Override
    public int execute() throws ExecuterException {
        return 0;
    }
}
