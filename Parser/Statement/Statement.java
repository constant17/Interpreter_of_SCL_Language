package Parser.Statement;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class ArithmeticExpression and the main method
 */

public abstract class Statement {

    public abstract String convertToStringTree(String indent);

    public abstract void execute() throws ExecuterException;
}
