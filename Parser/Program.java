package Parser;

import Executer.ExecuterException;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson Project: Deliverable 2
 * Course: CS7843 Concept of Programming language Lecturer: Jose Garrido Defines
 * the class ArithmeticExpression and the main method
 */

public class Program {

    private Block programCode;
    private String programId;
    private String import_statement;
    public Program(String importStatement, Block programCode, String prog){
        this.import_statement = importStatement;
        this.programCode = programCode;
        this.programId = prog;
    }

    public String toString(){
        String s = import_statement+ "\nFunction : "+programId+"\n"+ 
                programCode.convertToTreeString("|\t")+"endfunction "+programId;
        return s;
    }

    public void execute() throws ExecuterException {
        programCode.execute();
    }
}
