package Parser.Expression;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson Project: Deliverable 2
 * Course: CS7843 Concept of Programming language Lecturer: Jose Garrido Defines
 * the class SCLScanner and the main method
 */

public class BooleanExpression {

    private String relativeOperator;
    private ArithmeticExpression expression1;
    private ArithmeticExpression expression2;

    public BooleanExpression(String relativeOperator, ArithmeticExpression expression1, ArithmeticExpression expression2){
        this.relativeOperator = relativeOperator;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public String toString() {
        return "relative Operator: "+relativeOperator+"\n\t"+
                expression1+"\n\t"+expression2;
    }

    public String convertToStringTree(String indent){
        String s = indent+"Relative operator: "+relativeOperator+"\n"+
                indent+"Expression 1:\n"+expression1.convertToStringTree(indent+"\t")+"\n"+
                indent+"Expression 2:\n"+expression2.convertToStringTree(indent+"\t");
        return s;
    }

    public boolean execute() throws ExecuterException {
        boolean b = false;
        switch (relativeOperator){
            case "<=":
                b = expression1.execute() <= expression2.execute();
                break;
            case "<":
                b = expression1.execute() < expression2.execute();
                break;
            case ">":
                b = expression1.execute() > expression2.execute();
                break;
            case ">=":
                b = expression1.execute() >= expression2.execute();
                break;
            case "==":
                b = expression1.execute() == expression2.execute();
                break;
            case "~=":
                b = expression1.execute() != expression2.execute();
                break;
        }
        return b;
    }
}
