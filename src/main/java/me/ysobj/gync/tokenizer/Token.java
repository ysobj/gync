package me.ysobj.gync.tokenizer;

public class Token {
	public static enum TokenType {
		IDENTIFIER, STRING, NUMBER, OPERATOR, COMMA, PAREN_OPEN, PAREN_CLOSE, BRACE_OPEN, BRACE_CLOSE, TERMINATOR, EOL, EOF, OTHER;

		@Override
		public String toString() {
			return this.name();
		}

	}

	public static Token EOF = new Token(TokenType.EOF);

	private String original;

	private int position;

	private TokenType type;

	public TokenType getType() {
		return type;
	}

	public static Token create(String str, int currentTokensStart, TokenType tokenType) {
		return new Token(str, currentTokensStart, tokenType);
	}

	protected Token(TokenType tokenType) {
		this.type = tokenType;
	}

	protected Token(String str, int currentTokensStart, TokenType tokenType) {
		this.original = str;
		this.position = currentTokensStart;
		this.type = tokenType;
	}

	public String getOriginal() {
		return original;
	}
}
