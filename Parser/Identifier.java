package Parser;
/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class Display 
 */

import Executer.ExecuterException;
import Parser.Expression.ArithmeticExpression;
import Scanner.SCLScanner;

public class Identifier extends ArithmeticExpression {

    private String id;
    private String value;
    private String type;

    public Identifier(String id, String val, String typ) {
        this.id = id;
        this.value = val;
        this.type = typ;
    }

    public String getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public String getType() {
        return this.type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public String convertToStringTree(String indent) {
        String s = indent + "Identifier: " + id;
        return s;
    }

    @Override
    public int execute() throws ExecuterException {
        
            return Integer.parseInt(SCLScanner.identifiers_table.get(this.id).getValue());
    }
}
