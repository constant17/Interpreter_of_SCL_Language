package Parser.Statement;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class ArithmeticExpression and the main method
 */

import Parser.Block;
import Parser.Expression.BooleanExpression;

public class WhileStatement extends Statement{

    private BooleanExpression condition;
    private Block blockCode;

    public WhileStatement(BooleanExpression condition, Block blockCode){
        this.condition = condition;
        this.blockCode = blockCode;
    }

    @Override
    public String toString() {
        String s = "While Statement:"+"\n"+
                "\tCondition:\n\t\t"+condition.toString()+"\n"+
                "\tBlock code:\n\t\t"+blockCode;
        return s;
    }


    @Override
    public String convertToStringTree(String indent) {
        String s = indent+"While Statement:\n"+
                indent+"|\tCondition:\n"+condition.convertToStringTree(indent+"|\t|\t")+"\n"+
                indent+"|\tBlock Code:\n"+blockCode.convertToTreeString(indent+"|\t|\t");
        return s;
    }

    @Override
    public void execute() throws ExecuterException {
        while (condition.execute()){
            blockCode.execute();
        }
    }
}
