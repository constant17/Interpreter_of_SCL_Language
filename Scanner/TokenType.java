package Scanner;
/***
 * Author: Constant Pagoui, Kelley Roberts, Alex Nelson
 * Project: Deliverable 2
 * Course: CS7843 Concept of Programming language
 * Lecturer: Jose Garrido
 * Defines different token types in our program.
 */
public enum TokenType {
	
	    PROGRAM_TKN,
       import_statement,
	    for_statement,
	    identifier,
	    integer_literal,
	    keyword,
	    string_literal,
	    add_operator,
	    sub_operator,
	    multiply_operator,
	    division_operator,
	    assignment_operator,
	    set_statement,
       display_statement,
       input_statement,
       symbol_keyword,
       implementations_keyword,
       description_keyword,
       function_keyword,
       to_keyword,
       do_keyword,
       is_keyword,
       integer_keyword,
       variables_keyword,
       of_keyword,
       type_keyword,
       begin_keyword,
       if_statement,
       endfun_keyword,
       endwhile_keyword,
       define_statement,
       repeat_statement,
		left_parenthesis,
		right_parenthesis,
		le_operator,
		lt_operator,
		ge_operator,
		gt_operator,
    	eq_operator, 
		comments_bracket,
		arithmetic_expression,
		concat_operator,
		while_statement,
		exit_statement,
		EOS_TOK,
	
}
