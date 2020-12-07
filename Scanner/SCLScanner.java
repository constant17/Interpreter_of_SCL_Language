package Scanner;

/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines the class SCLScanner and the main method
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.io.PrintWriter;
import Parser.Identifier;;

public class SCLScanner {

	/**
	 * Private attributes of the class SCLScanner
	 * tokens
	 */
	private List<Token> tokens;
	private List<String> keywords;
	private String filename;
	private final int TKN_INDEX = 0;
	private final int LOOKAHEAD_INDEX = 1;
	// Public and Static Attributes
	public static HashMap<String, Identifier> identifiers_table; //creating the identifiers table
	
	public SCLScanner(String file_name) { 
		this.filename = file_name;
		assert(filename != null);
		tokens = new ArrayList<Token>();
		// instantiating the identifiers table
		identifiers_table = new HashMap<String, Identifier>();
		// List of keywords
		keywords = new ArrayList<String>(Arrays.asList("symbol", "forward","references", "function", "pointer", "array",
										"type", "struct", "integer", "enum", "global", "declarations", "implementations",
										"parameters","constant", "begin", "endfun", "enfor", "if", "else", "endif", "repeat", "until",
										"endrepeat", "display", "set",  "return", "is", "description", "variables"));
		try{
			this.tokens = lexicalAnalyzer();
		}
		catch(FileNotFoundException fnfe){
			System.out.println(fnfe);
		}
		catch(LexicalException le){
			System.out.println(le);
		}
	}

	public List<Token> lexicalAnalyzer() throws FileNotFoundException, LexicalException  {
      // Getting the file content into  a buffer reader
		BufferedReader sourceProgram = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/"+this.filename));
		int lineNum = 0; 
		String getLine;
		try{
			// Reads each line of the file
			while((getLine = sourceProgram.readLine()) != null) {
				//remove space at the begining and end of each line
				getLine = getLine.trim();
				//process line and get each token type
				scanLine(getLine, lineNum);
				//next line counter
				lineNum++;
			}
			//When done reading, add End Of String token type at the end of output
			tokens.add(new Token(lineNum+1, 1, "EOS", TokenType.EOS_TOK));
			//close buffer reader
			sourceProgram.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
      return this.tokens;
   }
	/***
    * method: getToken
     * get the first token in tokenList
     * @return Token
     */
    public Token getToken(){
        return this.tokens.get(this.TKN_INDEX);
    }
    /***
     * method: getLookAheadToken
     * get the next token in tokenList
     * @return Token
     */
    public Token getLookAheadToken(){
        return this.tokens.get(this.LOOKAHEAD_INDEX);
    }
    
    /***
     * method: removeToken
     * remove the first token in tokenList
     * @return Token
     */
    public Token removeToken(){
        return this.tokens.remove(this.TKN_INDEX);
    }
	/*
	 * Function scanLine : processes lines of the source code
	 * input: a String of chars representing line in the source code, 
	 * 		  an integer representing the line number
	 * return: void
	 * **/
	private void scanLine(String line, int lineNumber) throws LexicalException{
		assert(line != null && lineNumber >= 1);
		int index = 0;
		
		//Go through each word or lexeme
		while(index < line.length()) {
			//Get each lexeme from getLexeme() method
			String lexeme = getLexeme(line, lineNumber, index);
			//Get token type from getTokenType method
			TokenType tkType = getTokenType(lexeme, lineNumber, index);
			//Add token line number, column, lexeme, and type to tokens list
			tokens.add(new Token(lineNumber+1, index + 1, lexeme, tkType));
			//Get next lexeme
			index += lexeme.length();
			index = removeSpace(line, index);
		}
	}
	
	/**
	   * This is the getTokenType method which checks for token validity and return its type.
	   * @param args lexeme, line number and column number.
	   * @return TokenType: the type of the token
	   * @exception LexicalException on invalid lexeme error.
	   */
	private TokenType getTokenType(String lex, int lineNumber, int col) throws LexicalException{
		if(lex.contains("//") || lex.contains("/*") || lex.contains("*/")){
			//lex = lex.replace("//", "");
			//lex = lex.replace("/*", "");
			//lex = lex.replace("*/", "");
			//if(lex.equals("") || lex.equals(" "))
				return TokenType.comments_bracket;
		}
		assert(lex != null && lineNumber >= 1 && col >= 1);
		TokenType tokType = null;
		// selections for Keywords
		if(lex.equals("symbol"))
			return TokenType.symbol_keyword;
      else if(lex.equals("import"))
			return TokenType.import_statement;
      else if(lex.equals("implementations"))
			return TokenType.implementations_keyword;
      else if(lex.equals("description"))
			return TokenType.description_keyword;
      else if(lex.equals("function"))
			return TokenType.function_keyword;
      else if(lex.equals("endfun"))
			return TokenType.endfun_keyword;
      else if(lex.equals("is"))
			return TokenType.is_keyword;
      else if(lex.equals("set"))
			return TokenType.set_statement;
      else if(lex.equals("variables"))
			return TokenType.variables_keyword;
      else if(lex.equals("input"))
			return TokenType.input_statement;
      else if(lex.equals("do"))
			return TokenType.do_keyword;
      else if(lex.equals("define"))
			return TokenType.define_statement;
      else if(lex.equals("while"))
			return TokenType.while_statement;
      else if(lex.equals("endwhile"))
			return TokenType.endwhile_keyword;
      else if(lex.equals("of"))
			return TokenType.of_keyword;
      else if(lex.equals("display"))
			return TokenType.display_statement;
      else if(lex.equals("type"))
			return TokenType.type_keyword;
      else if(lex.equals("integer"))
			return TokenType.integer_keyword;
      else if(lex.equals("begin"))
			return TokenType.begin_keyword;
      else if(keywords.contains(lex))
         return TokenType.keyword;
		
		//selections for Statements
		else if(lex.equals("for")) 
			return TokenType.for_statement;
		else if(lex.equals("exit")) 
			return TokenType.exit_statement;	
		else if(lex.equals("while")) 
			return TokenType.while_statement;
		
		//selections for Identitfiers and arithmetic statements
		if(Character.isLetter(lex.charAt(0))) {
			if(lex.length() == 1) {
				if(isValidIdentifier(lex))
					tokType =  TokenType.identifier;
				else
					throw new LexicalException("Invalid lexeme at row "+(lineNumber+1)+" and column "+ (col+1)+" near lexeme: "+lex);
			}
			else if(isValidIdentifier(lex))
				tokType = TokenType.identifier;
			else if(lex.charAt(0)=='"' || lex.charAt(lex.length()-1) == '"')
			   tokType =  TokenType.string_literal;
			else
				throw new LexicalException("Invalid lexeme at row "+(lineNumber+1)+" and column "+ (col+1)+" near lexeme: "+lex);
		}
		// selections for digits
		else if(Character.isDigit(lex.charAt(0))) {
			if(digitOnly(lex))
				tokType =  TokenType.integer_literal;
			else if(isArithmeticExpression(lex)) 
				tokType = TokenType.arithmetic_expression;
			else if(isValidIdentifier(lex))
				tokType = TokenType.identifier;
			else
				throw new LexicalException("Invalid lexeme at row "+(lineNumber+1)+" and column "+ (col+1)+"! Not a number."+" near lexeme: "+lex);
		}
		
		// selections for operators
		else if(lex.charAt(0)=='"' || lex.charAt(lex.length()-1) == '"')
			tokType =  TokenType.string_literal;

		// selections for arithmetic operators
		else if(lex.equals("+"))
			return TokenType.add_operator;
		else if(lex.equals("-"))
			return  TokenType.sub_operator;
		else if(lex.equals("*"))
			return  TokenType.multiply_operator;
		else if(lex.equals("/"))
			return  TokenType.division_operator;
		else if(lex.equals("="))
			return  TokenType.assignment_operator;
		else if(lex.equals(","))
			return  TokenType.concat_operator;
		else if(lex.equals(">"))
			return  TokenType.gt_operator;
		else if(lex.equals("<"))
			return  TokenType.lt_operator;
		else if(lex.equals("<="))
			return  TokenType.le_operator;
		else if(lex.equals(">="))
			return  TokenType.ge_operator;

		// selections for brackets
		else if(lex.charAt(0) == '[' || lex.charAt(0) == '{'){
			lex = lex.replace("[","");
			lex = lex.replace("]", "");
			if(isValidIdentifier(lex))
				tokType = TokenType.identifier;
			else if(isArithmeticExpression(lex)) 
				tokType = TokenType.arithmetic_expression;
		}
		else if(lex.contains("[") && isValidIdentifier(lex)) 
				tokType = TokenType.identifier;	
		
		
		else
			throw new LexicalException("Invalid lexeme at row "+(lineNumber+1)+" and column "+ (col+1)+" near lexeme: "+lex);
		
		return tokType;
	}
	
	/**
	   * digitOnly method checks if a lexeme contains digits only
	   * @param args lexeme String
	   * @return boolean
	   * @exception none
	   */
	private boolean digitOnly(String str) {
		assert(str != null);
		int i = 0;
		while( i < str.length() && Character.isDigit(str.charAt(i))) 
			i++;
		return i==str.length();
	}
	
	/**
	   * getLexeme method .
	   * @param args String line, int lineNumber, int indx.
	   * @return String
	   * @exception none.
	   */
	private String getLexeme(String line, int lineNumber, int indx) {
		assert(line != null && lineNumber >= 1 && indx >= 0);
		int i = indx;
		
		while(i < line.length() && !Character.isWhitespace(line.charAt(i)))
			i++;
		return line.substring(indx, i);
	}
	
	/**
	   * removeSpace method removes blank space in lexeme
	   * @param args lexeme String, int starting index
	   * @return the index of the non blank space in lexeme
	   * @exception none
	   */
	private int removeSpace(String line, int ind) {
		assert(line != null && ind >= 0);
		while(ind < line.length() && Character.isWhitespace(line.charAt(ind)))
			ind++;
		return ind;
	}
	/**
	   * isValidIdentifier method checks if a lexeme is a valid identifier
	   * @param args lexeme String
	   * @return boolean, true if it is and false otherwise
	   * @exception none
	   */
	private boolean isValidIdentifier(String lex) {
		int i = 0;
		while(i < lex.length()) {
			if(!Character.isLetter(lex.charAt(i)) &&  lex.charAt(i) != '_' && lex.charAt(i) != ':' 
					&& lex.charAt(i) != '.' && lex.charAt(i) != '[' && lex.charAt(i) != ']')
				return false;
			i++;
		}
		return true;
	}
	
	/**
	   * isArithmeticExpression method checks if a lexeme is an arithmetic expression
	   * @param args lexeme String
	   * @return boolean, true if it is and false otherwise
	   * @exception none
	   */
	private boolean isArithmeticExpression(String lex) {
		int i = 0; boolean consec_op = false;
		while(i < lex.length()) {
			if (Character.isDigit(lex.charAt(i)) || Character.isLetter(lex.charAt(i)))
				consec_op = false;
			else if( (lex.charAt(i) != '-' && lex.charAt(i) != '+'
						&& lex.charAt(i) != '*' && lex.charAt(i) != '/' && lex.charAt(i) != '%') || consec_op) {
					return false;
			}
			else
				consec_op = true;
			i++;
		}
		return true;
	}
	/**
	   * lexicalAnalyzerOutput() method writes the result of the analysis in a file
	   * output.txt
	   * @param args none
	   * @return none
	   * @exception none
	   */
	public void lexicalAnalyzerOutput() {
		try {
         this.tokens = lexicalAnalyzer();
			String output = "", filenam = "scanner_output_"+filename.substring(0,filename.indexOf("."))+".txt";
			for(int i = 0; i < tokens.size(); i++) 
				output += tokens.get(i).toString()+"\n";
			File file = new File(filenam);
			if(!file.exists())
				file.createNewFile();
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(output);
			System.out.println(output);
			writer.close();

			}
		   catch (IOException ioe) {
				ioe.printStackTrace();
			}	
        
         catch (LexicalException le) {
				le.printStackTrace();
			}
	}
	/**
	   * main method gets the filename from console and create an 
	   * instance of the SCLScanner class.
	   * @param args none
	   * @return none
	   * @exception none
	   */
	public static void main(String args[]) {
		String fileName;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.print("Enter file name or hit Enter key to exit>");
			fileName = sc.nextLine();
			System.out.println("Start reading to scan file: "+fileName);
			try {
				SCLScanner scls = new SCLScanner(fileName);
				scls.lexicalAnalyzerOutput();
				System.out.println("Lexical analysis of the source code "+fileName+" has been written in the output.txt file ");
			}
			catch(Exception e) {
				System.out.println(e);
			}
			System.out.println();
		}while(fileName != null && !fileName.equals(""));
		sc.close();
	}
}