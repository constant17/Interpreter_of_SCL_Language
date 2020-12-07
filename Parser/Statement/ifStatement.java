package Parser.Statement;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class Display 
 */

import Parser.Block;
import Parser.Expression.BooleanExpression;

public class ifStatement extends Statement {

    private BooleanExpression condition;
    private Block ifBlock;
    private Block elseBlock;

    public ifStatement(BooleanExpression condition, Block ifBlock, Block elseBlock){
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public String toString(){
        String s = "if Statement:\n";
        s+= "\tCondition: "+condition.toString()+"\n"+
                "\tIf Block:\n\t"+ifBlock.toString()+""+
                "\tElse Block:\n\t"+elseBlock.toString();
        return s;
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = indent+"If Statement:\n"+
                indent+"|\tCondition:\n"+condition.convertToStringTree(indent+"|\t|\t")+"\n"+
                indent+"|\tIf Block:\n"+ifBlock.convertToTreeString(indent+"|\t|\t")+
                indent+"|\tElse Block:\n"+elseBlock.convertToTreeString(indent+"|\t|\t");
        return s;
    }

    @Override
    public void execute() throws ExecuterException {
        boolean b = condition.execute();
        if(elseBlock!=null){
            if(b)
                ifBlock.execute();
            else
                elseBlock.execute();
        }else {
            if (b)
                ifBlock.execute();
        }
    }

}
