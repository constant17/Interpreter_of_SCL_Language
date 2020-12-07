package Parser.Statement;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class Display 
 */

import Parser.Expression.ArithmeticExpression;

public class DisplayStatement extends Statement{

    private ArithmeticExpression expression;
    private String literalString;
    public DisplayStatement(ArithmeticExpression expression, String strLiteral) {
        this.expression = expression;
        this.literalString = strLiteral;
    }

    @Override
    public String toString() {
        return "Display Statement: display\n\t"+expression.toString();
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = "";
        if(expression != null) 
        s = indent+"Display Statement: display\n"+"|\t"+indent +"String literal: "+literalString+"\n"
            +expression.convertToStringTree(indent+"|\t");
        else s = indent+"Display Statement: display\n"+"|\t"+indent +"String literal: "+literalString;
        return s;
    }

    @Override
    public void execute() throws ExecuterException {
        if( expression != null)
            System.out.println(literalString.substring(1, literalString.length()-2)+ " "+expression.execute());
        else
            System.out.println(literalString.substring(1, literalString.length()-2));
        //Interpreter.addElementToOutPut(Integer.toString(result));
    }
}
