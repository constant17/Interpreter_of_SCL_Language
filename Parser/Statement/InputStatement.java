package Parser.Statement;

import java.util.Scanner;

import Executer.ExecuterException;
import Parser.Identifier;
import Scanner.SCLScanner;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Pr. Jose Garrido
 * Defines the class Display 
 */


public class InputStatement extends Statement{

    private Identifier id;
    private String literalString;
    public InputStatement(Identifier ident, String strLiteral) {
        this.id = ident;
        this.literalString = strLiteral;
    }

    @Override
    public String toString() {
        return "Input Statement: input\n\t"+id.getId();
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = "";
        if(id != null) 
        s = indent+"Input Statement: input\n"+"|\t"+indent +"String literal: "+literalString+"\n"
            +id.convertToStringTree(indent+"|\t");
        else s = indent+"Input Statement: input\n"+"|\t"+indent +"String literal: "+literalString;
        return s;
    }

    @Override
    public void execute() throws ExecuterException {
        Scanner sc = new Scanner(System.in);
        System.out.print(literalString.substring(1, literalString.length()-2)+" >_");
        String temp = "";
        if(sc.hasNext())
            temp = sc.next(); 
        else
            throw new ExecuterException("Invalid input!!");
        id.setValue(temp);
        SCLScanner.identifiers_table.put(id.getId(), id);
    }
}
