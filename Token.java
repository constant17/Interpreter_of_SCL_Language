/**
 * @author lpagoui
 *
 */
public class Token {

	/**
	 * 
	 */
	// Instance variables
	private int rowNumber;
	private int colNumber;
	private TokenType tokenType;
	private String lexeme;
	
	//Default constructor
	public Token() {
		// TODO Auto-generated constructor stub
	}
	
	// Token class constructor
	public Token(int rowNum, int colNum, String lexem, TokenType tType) {
		
		if(rowNum <= 0) 
			throw new IllegalArgumentException("Invalid row number!");
		if(colNum <= 0) 
			throw new IllegalArgumentException("Invalid column number!");
		if(lexem == null || lexem.length() == 0)
		    throw new IllegalArgumentException("Invalid lexeme argument");
		if(tType == null)
		    throw new IllegalArgumentException("Invalid token type argument");
		
		this.rowNumber = rowNum;
		this.tokenType = tType;
		this.lexeme = lexem;
		this.colNumber = colNum;
	}
	
	// Getters
	public String getLexeme(){
		return this.lexeme; 
	}
	public int getRowNumber(){
		return this.rowNumber; 
	}
	public int getColumnNumber(){
		return this.colNumber; 
	}
	public TokenType getTokenType(){
		return this.tokenType; 
	}
	
	// ToString Method
	public String toString(){
		return "Type: "+this.getTokenType()+ ",  token: "+this.getLexeme()+ "  line: "+this.getRowNumber()+ " col: "+this.getColumnNumber();
	}
}
