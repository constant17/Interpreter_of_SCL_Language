package Parser.Expression;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson Project: Deliverable 2
 * Course: CS7843 Concept of Programming language Lecturer: Jose Garrido Defines
 * the class ArithmeticExpression
 */

public abstract class ArithmeticExpression {

    public abstract String convertToStringTree(String indent);

    public abstract int execute() throws ExecuterException;
}
