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
		Tokenizer tokenizer = new Tokenizer(
				"func fib(x){if(x < 2){return x;}else{ return fib(x-1)+fib(x-2); }};a = fib(10);print(a);");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "func");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "fib");
		testHelper(tokenizer, Token.TokenType.PAREN_OPEN, "(");
		testHelper(tokenizer, Token.TokenType.IDENTIFIER, "x");
		testHelper(tokenizer, Token.TokenType.PAREN_CLOSE, ")");
	}

	protected void testHelper(Tokenizer tokenizer, Token.TokenType type, String original) {
		Token actualToken = tokenizer.next();
		assertEquals(type, actualToken.getType());
		assertEquals(original, actualToken.getOriginal());
	}

}
