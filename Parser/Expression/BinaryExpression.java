package Parser.Expression;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson Project: Deliverable 2
 * Course: CS7843 Concept of Programming language Lecturer: Jose Garrido Defines
 * the class BinaryExpression
 */

public class BinaryExpression extends ArithmeticExpression{

    private char operator;
    private ArithmeticExpression expression1;
    private ArithmeticExpression expression2;

    public BinaryExpression(ArithmeticExpression expression1, char operator,  ArithmeticExpression expression2) {
        this.operator = operator;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }
    public String getExpression1(){return this.expression1.toString();}
    public String getExpression2(){return this.expression2.toString();}
    @Override
    public String toString() {
        String s = "Binary Expression:"+"\n"+
                "\t"+"Expression 1:\n\t\t"+expression1+"\n"+
                "\t"+"Operator: "+operator+"\n"+
                "\t"+"Expression 2:\n\t\t"+expression2;
        return s;
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = indent+"Binary Expression:\n"+
                indent+"|\tExpression 1:\n"+expression1.convertToStringTree(indent+"|\t|\t")+"\n"+
                indent+"|\tOperator: "+operator+"\n"+
                indent+"|\tExpression 2:\n"+expression2.convertToStringTree(indent+"|\t|\t");
        return s;
    }

    @Override
    public int execute() throws ExecuterException {
        int result = 0;
        switch (operator){
            case '+':
                result = expression1.execute()+expression2.execute();
                break;
            case '-':
                result = expression1.execute()-expression2.execute();
                break;
            case '*':
                result = expression1.execute()*expression2.execute();
                break;
            case '/':
                result = expression1.execute()/expression2.execute();
                break;
        }
        return result;
    }

}
