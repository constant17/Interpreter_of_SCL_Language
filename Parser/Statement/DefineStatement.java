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

public class DefineStatement extends Statement{

    private ArithmeticExpression expression;
    private String type;
    public DefineStatement(ArithmeticExpression expression, String typ) {
        this.expression = expression;
        this.type = typ;
    }

    @Override
    public String toString() {
        return "Define Statement: define\n\t"+expression.toString();
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = indent+"Define Statement: define\n"+"|\t"+ "|\tType: "+type+"\n"
            +expression.convertToStringTree(indent+"|\t");
        return s;
    }

    @Override
    public void execute() throws ExecuterException {
        
        //Interpreter.addElementToOutPut(Integer.toString(result));
    }
}
