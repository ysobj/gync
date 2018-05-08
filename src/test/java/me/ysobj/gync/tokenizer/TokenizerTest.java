package me.ysobj.gync.tokenizer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Basic testcase")
	void test() throws Exception {
		String[] operators = new String[] {"+","-","*","/","=","<",">","<=",">=","==","!="};
		String[] keywords = new String[] {};
		Tokenizer tokenizer = new Tokenizer(
				"func fib(x){if(x < 2){return x;}else{ return fib(x-1)+fib(x-2); }};a = fib(10);print(a);",operators,keywords);
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "func");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "fib");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "x");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
		testHelper(tokenizer, Token.TokenType.BRACE_OPEN, "{");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "if");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "x");
		testHelper(tokenizer, Token.TokenType.OPERATOR, "<");
		testHelper(tokenizer, Token.TokenType.NUMBER, "2");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
		testHelper(tokenizer, Token.TokenType.BRACE_OPEN, "{");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "return");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "x");
		testHelper(tokenizer, Token.TokenType.TERMINATOR, ";");
		testHelper(tokenizer, Token.TokenType.BRACE_CLOSE, "}");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "else");
		testHelper(tokenizer, Token.TokenType.BRACE_OPEN, "{");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "return");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "fib");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "x");
		testHelper(tokenizer, Token.TokenType.OPERATOR, "-");
		testHelper(tokenizer, Token.TokenType.NUMBER, "1");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
		testHelper(tokenizer, Token.TokenType.OPERATOR, "+");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "fib");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "x");
		testHelper(tokenizer, Token.TokenType.OPERATOR, "-");
		testHelper(tokenizer, Token.TokenType.NUMBER, "2");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
		testHelper(tokenizer, Token.TokenType.TERMINATOR, ";");
		testHelper(tokenizer, Token.TokenType.BRACE_CLOSE, "}");
		testHelper(tokenizer, Token.TokenType.BRACE_CLOSE, "}");
		testHelper(tokenizer, Token.TokenType.TERMINATOR, ";");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "a");
		testHelper(tokenizer, Token.TokenType.OPERATOR, "=");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "fib");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.NUMBER, "10");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
		testHelper(tokenizer, Token.TokenType.TERMINATOR, ";");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "print");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "a");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
		testHelper(tokenizer, Token.TokenType.TERMINATOR, ";");
	}

	protected void testHelper(Tokenizer tokenizer, Token.TokenType type, String original) {
		Token actualToken = tokenizer.next();
		assertEquals(type, actualToken.getType());
		assertEquals(original, actualToken.getOriginal());
	}

}
