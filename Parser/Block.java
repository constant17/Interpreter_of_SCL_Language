package Parser;

/**
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class Display 
 */

import Parser.Statement.Statement;

import java.util.LinkedList;

import Executer.ExecuterException;

public class Block {

    private LinkedList<Statement> listStatement;

    public Block(){
        listStatement = new LinkedList<>();
    }

    public LinkedList<Statement> getListStatement() {
        return listStatement;
    }

    public void addStatement(Statement statement){
        listStatement.add(statement);
    }

    public void executeBlock(){

    }

    public String toString(){
        String s = "";
        for (Statement statement: listStatement) {
            s+=statement.toString()+"\n";
        }
        return s;
    }

    public String convertToTreeString(String indent){
        String s = "";
        for (Statement statement: listStatement) {
            s+=statement.convertToStringTree(indent)+"\n";
        }
        return s;
    }

    public void execute() throws ExecuterException {
        for (Statement statement: listStatement) {
            statement.execute();
        }
    }
}
