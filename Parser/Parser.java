package Parser;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Pr. Jose Garrido
 * Program: Defines the class Parser
 */

import Scanner.SCLScanner;
import Scanner.Token;
import Scanner.TokenType;
import Scanner.LexicalException;
import Parser.Expression.*;
import Parser.Statement.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/****************
 * class: Parser - creates an instance of the SCLScanner class and calls its lexicalAnalyzer
 *          method to get the list of lexemes in the source code.
 * attributes: identifier_table, keyword, currentToken, lokkAheadTkn, tokens
 */
public class Parser{
   // Attributes
   private SCLScanner scanner;
   
   // Constructor
   public Parser(String filename) throws LexicalException, IOException{
         this.scanner = new SCLScanner(filename);
	}
    /***
     * method: getBlockCode
     * @return : Block containing the statements inside it.
     * @throws SyntaxException
     */
    private Block getBlockCode() throws SyntaxException {
        if(scanner.getToken().getTokenType().equals(TokenType.variables_keyword)){
            scanner.removeToken();
        }
        Block block = new Block();
        Token token = scanner.getToken(); //get token from the scanner
        while (isValidToken(token)){
            
            Statement statement = getStatement();
            block.addStatement(statement);
            token = scanner.getToken();//get next token from the scanner
            if(token.getTokenType().equals(TokenType.begin_keyword)){
                scanner.removeToken();
                token = scanner.getToken(); //get next token from the scanner
            }
        }
        return block; // return the block
    }

    /***
     * getStatement(): method to get the different statements inside a block of code.
     * @return Statement
     * @throws SyntaxException
     */
    private Statement getStatement() throws SyntaxException {
        
        Statement statement = null;
        Token token = scanner.getToken(); //get token from the scanner
        TokenType tknType = token.getTokenType(); // get toke type
        if(tknType.equals(TokenType.define_statement))
            statement = getDefineStatement();
        else if(tknType.equals(TokenType.display_statement))
            statement = getDisplayStatement();
        else if(tknType.equals(TokenType.input_statement))
            statement = getInputStatement();
            else if(tknType.equals(TokenType.set_statement))
            statement = getSetStatement();
        else if(tknType.equals(TokenType.while_statement))
            statement = getWhileStatement();
        else if(tknType.equals(TokenType.if_statement))
            statement = getDefineStatement();
        
        return statement;
    }

   
    /***
     * getDefinedStatement(): method handling the if_else statement:
     * <if_statement>->if<boolean_expression>then<block>else<block>
     * @return ifStatement object
     * @throws SyntaxException
     */
    private Statement getDefineStatement() throws SyntaxException {
        checkToken(scanner.removeToken(),TokenType.define_statement);
        checkToken(scanner.getToken(),TokenType.identifier);
        // variables to store the characteristics of the identifier
        String id_name = scanner.removeToken().getLexeme(), id_value = "0"; 
        checkToken(scanner.removeToken(),TokenType.of_keyword);
        checkToken(scanner.removeToken(),TokenType.type_keyword);
        String typee = scanner.getToken().getTokenType().equals(TokenType.integer_keyword) || 
                    scanner.getToken().getTokenType().equals(TokenType.string_literal) ?scanner.removeToken().getLexeme():"";
        //Add the defined identifier into the identifier table
        SCLScanner.identifiers_table.put(id_name, new Identifier(id_name, id_value, typee));
        //return the statement
        return new DefineStatement(new Identifier(id_name, id_value, typee), typee);
    }
    
    /***
     * getWhileStatement(): method handling the while statement:
     * <while_statement>->while<boolean_expression>do<block>end
     * @return WhileStatement object
     * @param None
     * @throws SyntaxException
     */
    private Statement getWhileStatement() throws SyntaxException {
        checkToken(scanner.removeToken(),TokenType.while_statement); // expecting while keyword
        BooleanExpression booleanExpression = getBooleanExpression();
        checkToken(scanner.removeToken(),TokenType.do_keyword); // expecting do keyword
        Block block = getBlockCode();
        //System.out.println(block.toString());
        checkToken(scanner.removeToken(),TokenType.endwhile_keyword); // expecting endwhile keyword
        return new WhileStatement(booleanExpression,block);
    }

     /***
     * getStatement(): method handling the assignment statement:
     * <assignment_statement>-><identifier><assignment_operator><arithmetic_Expression>
     * @return Assignment Statement
     * @param None
     * @throws SyntaxException
     */
    private Statement getSetStatement() throws SyntaxException {
        scanner.removeToken();
        Token token = scanner.getToken();
        checkToken(token,TokenType.identifier); // expecting an identifier after keyword set
        String id_value = SCLScanner.identifiers_table.get(scanner.getToken().getLexeme()).getValue();
        String id_type = SCLScanner.identifiers_table.get(scanner.getToken().getLexeme()).getType();
        Identifier identifier = new Identifier(scanner.removeToken().getLexeme(),id_value,id_type);
        token = scanner.getToken();
        checkToken(token,TokenType.assignment_operator);
        scanner.removeToken();
        BinaryExpression expression = getBinaryExpression();
        // computing the new value of the identifier being set
        System.out.println("First elt exp: "+expression.getExpression1().toString());
        System.out.println("Second elt exp: "+expression.getExpression2().toString());
        int result = Integer.parseInt(expression.getExpression1().toString()) + 
                        Integer.parseInt(expression.getExpression2().toString());
        //Update identifier value
        identifier.setValue(String.valueOf(result));
        //Update the identifiers table with the new value 
        SCLScanner.identifiers_table.put(identifier.getId(), identifier);
        return new SetStatement(identifier,expression);
    }

    /********************
     * checkComments(): method to check there are comments and remove them.
     * @return None
     * @param None
     */
    public void checkComments(){
        Token token = scanner.getToken();
        if(token.getTokenType().equals(TokenType.comments_bracket)){
            scanner.removeToken();
            token =scanner.removeToken(); TokenType typ = token.getTokenType();
            while(!typ.equals(TokenType.comments_bracket)){
                token = scanner.removeToken();  typ = token.getTokenType();
            }
        }
    }
    
    /***
     * method parse: calls the scanner repeatitively to check for tokens and gets the program blocks.
     * @return Program object representing the content of source code in parse tree format
     * @param None
     * @throws SyntaxException, ParsingException
     * @throws ParsingException
     */
    public Program parse() throws SyntaxException, ParsingException {
        checkComments();// check for comments
        String impStatements = checkImportStatement(); //check if there are import statements
        checkComments(); // check for comments
        checkToken(scanner.removeToken(), TokenType.symbol_keyword); // Expecting symbol keyword
        checkToken(scanner.getToken(),TokenType.identifier); // Expecting identifier representing the symbol
        // Expecting integer literal representing the symbol
        checkToken(scanner.getLookAheadToken(),TokenType.integer_literal); 
        //add the identifier, value and type to the identifiers table 
        SCLScanner.identifiers_table.put(scanner.getToken().getLexeme(), new Identifier(scanner.removeToken().getLexeme(), 
                                            scanner.removeToken().getLexeme(), "integer"));
        checkComments(); // check for comments
        checkToken(scanner.removeToken(), TokenType.description_keyword); // Expecting description keyword
        checkComments();  // check for comments
        checkToken(scanner.removeToken(), TokenType.function_keyword); // Expecting function keyword
        checkToken(scanner.getToken(),TokenType.identifier); // Expecting identifier representing the function name
        String idProgram = scanner.removeToken().getLexeme(); // 
        checkToken(scanner.removeToken(),TokenType.is_keyword);
        Block block = getBlockCode();
        checkToken(scanner.removeToken(),TokenType.endfun_keyword);
        checkToken(scanner.removeToken(),TokenType.identifier);
        if(!scanner.removeToken().getTokenType().equals(TokenType.EOS_TOK))
            throw new ParsingException("Reached End Of File while parsing");
        return new Program(impStatements, block, idProgram);
    }

    /***
     * isValidToke() method checking if the token we have is a continuation token, a token implying that
     * there is more lines to come and not the end of a block.
     * @param token
     * @return true if it's continuation token, false otherwise
     */
    private boolean isValidToken(Token token ){
        TokenType tknType = token.getTokenType();
        return tknType.equals(TokenType.while_statement)||tknType.equals(TokenType.repeat_statement)
                ||tknType.equals(TokenType.if_statement)||tknType.equals(TokenType.identifier)||
                tknType.equals(TokenType.begin_keyword)|| tknType.equals(TokenType.set_statement) ||
                tknType.equals(TokenType.display_statement) || tknType.equals(TokenType.concat_operator) ||
                tknType.equals(TokenType.define_statement) || tknType.equals(TokenType.input_statement)
                || tknType.equals(TokenType.assignment_operator) || tknType.equals(TokenType.string_literal);
    }

    /***
     * checkToken() method to Check if the token is a valid lexeme.
     * @param tokenToCheck
     * @param tokenTarget
     * @throws SyntaxException
     */
    private void checkToken(Token tokenToCheck, TokenType targetType) throws SyntaxException{
        TokenType tknType = tokenToCheck.getTokenType();
        if(!tknType.equals(targetType)) {
            if(targetType.equals(TokenType.identifier))
                throw  new SyntaxException("Expecting Identifier at line:" + tokenToCheck.getRowNumber() + " and column:" + tokenToCheck.getColumnNumber());
            else if (targetType.equals(TokenType.integer_literal))
                throw new SyntaxException("Expecting Integer Literal at line:" + tokenToCheck.getRowNumber() + " and column:" + tokenToCheck.getColumnNumber());
            else
                throw new SyntaxException("Expecting " + targetType + " at line:" + tokenToCheck.getRowNumber() + " and column:" + tokenToCheck.getColumnNumber());
        }
    }
    
    /***
     * checkImportStatement() to check for valid import statement 
     * @param tokenToCheck
     * @param tokenTarget
     * @return String representing the import statement
     * @throws SyntaxException
     */
    private String checkImportStatement() throws SyntaxException{
        TokenType type = scanner.getToken().getTokenType();
        Token look_ahead = scanner.getLookAheadToken(); String str = "";
        while(type.equals(TokenType.import_statement)){
            if((!look_ahead.getTokenType().equals(TokenType.string_literal)
                && !look_ahead.getTokenType().equals(TokenType.identifier))){
                throw  new SyntaxException("Expecting string literal at line:" + 
                look_ahead.getRowNumber() + " and column:" + look_ahead.getColumnNumber());
            }
            else if(look_ahead.getTokenType().equals(TokenType.string_literal)
            || look_ahead.getTokenType().equals(TokenType.identifier)){
            str += "Import Statement: import\n";
            if(look_ahead.getTokenType().equals(TokenType.string_literal))
            str += "|\tString literal: "+look_ahead.getLexeme();
            else str += "|\tLibrary: "+look_ahead.getLexeme();
            scanner.removeToken(); scanner.removeToken();
            type = scanner.getToken().getTokenType();
            look_ahead = scanner.getLookAheadToken();
            }
            else{scanner.removeToken(); scanner.removeToken(); break;}
        }
        return str;
    }
    /***
     * checkRelativeOperator() method to check if a token is a relative operator (<, >, <=, >=)
     * @param token
     * @return None
     * @throws SyntaxException
     */
    private void checkRelativeOperator(Token token) throws SyntaxException{
        TokenType tknType = token.getTokenType();
        boolean b = tknType.equals(TokenType.le_operator)||tknType.equals(TokenType.lt_operator)||tknType.equals(TokenType.ge_operator)
                     ||tknType.equals(TokenType.gt_operator)|| tknType.equals(TokenType.eq_operator);
        if(!b)
            throw new SyntaxException("Expecting relative operator at line:"+token.getRowNumber()+" and column:"+token.getColumnNumber());
    }
    
    /***
     * checkArithmeticOperator() check if a token is an operator
     * @param token
     * @throws SyntaxException
     */
    private void checkArithmeticOperator(Token token) throws SyntaxException {
        TokenType tknType = token.getTokenType(); 
        boolean b = tknType.equals(TokenType.add_operator)||tknType.equals(TokenType.multiply_operator)
        ||tknType.equals(TokenType.sub_operator)||tknType.equals(TokenType.division_operator);
        if(!b)
            throw new SyntaxException("Expecting operator at line:"+token.getRowNumber()+" and column:"+token.getColumnNumber());
    }
    
    /***
     * getDisplayStatememnt() method checks the print statement is a valid one.
     * <display_statement>->display <arithmetic_expression> | string_literal | integer_literal
     * @return Statement representing DisplayStatement 
     * @param None
     * @throws SyntaxException
     */
    private Statement getDisplayStatement() throws SyntaxException {
        checkToken(scanner.removeToken(),TokenType.display_statement);
        String str = checkStringLiteral(scanner.getToken());
        if(scanner.getToken().getTokenType().equals(TokenType.concat_operator)){
            scanner.removeToken();
            ArithmeticExpression expression = getArithmeticExpression();
            return new DisplayStatement(expression, str);
        }
        return new DisplayStatement (null, str);
    }

    /***
     * getInputStatement() method checks the print statement is a valid one.
     * <display_statement>->display <arithmetic_expression> | string_literal | integer_literal
     * @return Statement representing the input statement.
     * @throws SyntaxException
     */
    private Statement getInputStatement() throws SyntaxException {
        checkToken(scanner.removeToken(),TokenType.input_statement);
        String str = checkStringLiteral(scanner.getToken());
        if(scanner.getToken().getTokenType().equals(TokenType.concat_operator)){
            scanner.removeToken();
            Identifier identify = getIdentifier();
            return new InputStatement(identify, str);
        }
        return new DisplayStatement(null, str);
    }
    /***
     * checkStringLiteral() method checks the token is the beginning of string literal.
     * @return String representing the entire string literal.
     * @param Token the token being checked.
     * @throws SyntaxException
     */
    public String checkStringLiteral(Token token) throws SyntaxException {
        checkToken(token,TokenType.string_literal);
        String str = token.getLexeme()+" ";
        scanner. removeToken(); 
        TokenType tkn = scanner.getToken().getTokenType();
        while(!tkn.equals(TokenType.string_literal) && !tkn.equals(TokenType.EOS_TOK)){
            str += scanner.getToken().getLexeme()+" ";
            scanner.removeToken();
            tkn = scanner.getToken().getTokenType();
        }
        if(tkn.equals(TokenType.EOS_TOK))
        throw new SyntaxException("Expecting closing \" string literal at line:"+
        token.getRowNumber()+" and column:"+token.getColumnNumber());
        str += scanner.getToken().getLexeme()+" ";
        scanner.removeToken();
        return str;
    }

    private Identifier getIdentifier() throws SyntaxException{
        Token token = scanner.getToken();
        TokenType tknType = token.getTokenType();
        if(tknType.equals(TokenType.identifier)){
            //get the identifier value and type from the identifiers table 
            String id_value = SCLScanner.identifiers_table.get(token.getLexeme()).getValue();
            String id_type = SCLScanner.identifiers_table.get(token.getLexeme()).getType();
            // return the identifier
            return new Identifier(scanner.removeToken().getLexeme(), id_value, id_type);
        }
        else throw new SyntaxException("Syntax error! Expecting Identifier at row: "+ token.getRowNumber()+", column: "+token.getColumnNumber());
    }
    /***
     * getArithmeticExpression() core method handling the different kind of arithmeticExpression:
     * <arithmetic_expression>-><identifier>|<literal_integer>|<arithmetic_op><arithmetic_expression><arithmetic_expression>
     * @return ArithmeticExpression object
     * @param None 
     * @throws SyntaxException
     */
    private ArithmeticExpression getArithmeticExpression() throws SyntaxException {
        Token token = scanner.getToken();
        TokenType tknType = token.getTokenType();
        if(tknType.equals(TokenType.identifier))
            return getIdentifier();
        if(tknType.equals(TokenType.integer_literal))
            return new LitteralInteger(Integer.parseInt(scanner.removeToken().getLexeme()));
        if(tknType.equals(TokenType.string_literal))
            return new LiteralString(checkStringLiteral(token));
        
        else{
            BinaryExpression binaryExpression = getBinaryExpression();
            return binaryExpression;
        }
    }

    /***
     * getBinaryExpression() method building the tree for <arithmetic_expression>-><arithmetic_operator><arithmetic_expression><arit>
     * @return BinaryExpression Object
     * @param None
     * @throws SyntaxException
     */
    private BinaryExpression getBinaryExpression() throws SyntaxException {
        ArithmeticExpression expression1 = getArithmeticExpression();
        Token token = scanner.removeToken();
        checkArithmeticOperator(token);
        char operator = token.getLexeme().charAt(0);
        ArithmeticExpression expression2 = getArithmeticExpression(); 
        return new BinaryExpression(expression1,operator,expression2);
    }

    /***
     * getBooleanExpression() method building the tree for <boolean_Expression>-><relative_op><arithmetic_expression><arithmetic_expression>
     * @return BooleanExpression Object
     * @param None
     * @throws SyntaxException
     */
    private BooleanExpression getBooleanExpression() throws SyntaxException {
        Token token = scanner.getToken();
        ArithmeticExpression expression1 = getArithmeticExpression();
        token = scanner.getToken();
        checkRelativeOperator(token);
        String relativeOperator = scanner.removeToken().getLexeme();
        ArithmeticExpression expression2 = getArithmeticExpression();
        return new BooleanExpression(relativeOperator,expression1,expression2);
    }

    /***
     * main method: reads the filename from the console and instantiate an object of the Parser class
     *  that calls the Scanner to get the list of tokens. Then, prints the parser output in a file. 
     * @return None
     * @param String[] an array of arguments
     
    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("\n\nEnter filename: ");
        String filenam = sc.nextLine();
        try {
            Parser parser = new Parser(filenam);
            Program program = parser.parse();
            filenam = filenam.substring(0, filenam.indexOf("."))+"_parser_output.txt";
            File file = new File(filenam);
			if(!file.exists())
				file.createNewFile();
			PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(program);
            writer.close();
            System.out.println("\nThe result of the syntax analysis of the program and\n"+
            "the parse tree have been successfully written in the file "+filenam);
    } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (SyntaxException e) {
            e.printStackTrace();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (LexicalException le) {
            System.out.println(le);;
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.close();
    }  */
}