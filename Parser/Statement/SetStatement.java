package Parser.Statement;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class Display 
 */

import Parser.Expression.BinaryExpression;
import Scanner.SCLScanner;
import Executer.ExecuterException;
import Parser.Identifier;

public class SetStatement extends Statement {

    private Identifier identifier;
    private BinaryExpression expression;

    public SetStatement(Identifier identifier, BinaryExpression expression){
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
        String s = "Set Statement:"+
                "\n\tIndentifier: "+identifier.toString()+"\n\t"+
                expression.toString();
        return s;
    }

    @Override
    public String convertToStringTree(String indent) {
        String s =indent+"Set Statement: \n"+
                indent+"|\tIdentifier: "+identifier.getId()+"\n"+
                indent+"|\tOperator: =\n"+
                expression.convertToStringTree(indent+"|\t");
        return s;
    }

    @Override
    public void execute() throws ExecuterException {
        this.identifier.setValue(this.expression.execute()+"");
        SCLScanner.identifiers_table.put(this.identifier.getId(), this.identifier);
    }
}
