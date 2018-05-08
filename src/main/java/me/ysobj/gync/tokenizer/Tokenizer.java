package me.ysobj.gync.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import me.ysobj.gync.tokenizer.Token.TokenType;

public class Tokenizer {
	private Reader reader;
	private int currentTokensStart = 0;
	private List<Token> preReadTokens = new ArrayList<>();
	private String[] operators;
	private String[] keywords;

	private int preRead = -1;

	private static final int EOS = -1;

	private static final int SPACE = (int) ' ';

	private static final int QUOTE = (int) '\'';

	private static final int COMMA = (int) ',';

	private static final int SEMICOLON = (int) ';';

	private static final int PAREN_OPEN = (int) '(';

	private static final int PAREN_CLOSE = (int) ')';

	private static final int BRACE_OPEN = (int) '{';

	private static final int BRACE_CLOSE = (int) '}';

	private static final int DOT = (int) '.';

	private static final int LF = (int) '\n';

	public Tokenizer(String string) {
		this(string, new String[] {}, new String[] {});
	}

	public Tokenizer(String string, String[] operators, String[] keywords) {
		this.reader = new StringReader(string);
		this.operators = operators;
		this.keywords = keywords;
	}

	public Tokenizer(Reader reader) {
		this.reader = reader;
	}

	public boolean hasNext() {
		Token preReadToken = null;
		if (preReadTokens.size() > 0) {
			preReadToken = preReadTokens.get(0);
		}
		if (preReadToken == Token.EOF) {
			return false;
		}
		if (preReadToken != null) {
			return true;
		}
		if (this.reader == null) {
			return false;
		}
		Token t = next();
		if (t == Token.EOF) {
			return false;
		}
		preReadTokens.add(t);
		return true;
	}

	private int read() throws IOException {
		int r = -1;
		if (preRead != -1) {
			r = preRead;
			preRead = -1;
		} else {
			r = reader.read();
		}
		return r;
	}

	public Token peek() {
		if (hasNext()) {
			return this.preReadTokens.get(0);
		}
		return Token.EOF;
	}

	public Token next() {
		if (preReadTokens.size() > 0) {
			return preReadTokens.remove(0);
		}
		StringBuilder sb = new StringBuilder();
		int readLength = 0;
		boolean isOpen = false;
		boolean isNumeric = false;
		boolean isIdentifier = false;
		boolean isOperator = false;
		while (true) {
			try {
				int r = read();
				readLength++;
				switch (r) {
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '0':
					if (!isNumeric && !isIdentifier && sb.length() > 0) {
						this.preRead = r;
						return createToken(sb.toString(), readLength);
					}
					if (!isOpen && !isIdentifier) {
						isNumeric = true;
					}
					break;
				case DOT:
					if (!isNumeric) {
						if (sb.length() > 0) {
							this.preRead = r;
							return createToken(sb.toString(), readLength);
						} else {
							return createToken(String.valueOf((char) r), readLength);
						}
					}
					break;
				case '+':
				case '-':
				case '*':
				case '/':
				case '%':
				case '<':
				case '>':
				case '=':
					if (!isOperator && sb.length() > 0) {
						this.preRead = r;
						return createToken(sb.toString(), readLength);
					}
					isOperator = true;
					break;
				case COMMA:
				case PAREN_OPEN:
				case PAREN_CLOSE:
				case BRACE_OPEN:
				case BRACE_CLOSE:
				case SEMICOLON:
					if (sb.length() > 0) {
						this.preRead = r;
						return createToken(sb.toString(), readLength);
					} else {
						return createToken(String.valueOf((char) r), readLength);
					}
				case LF:
					if (sb.length() > 0) {
						return createToken(sb.toString(), readLength);
					}
					continue;
				case QUOTE:
					isOpen = !isOpen;
					break;
				case EOS:
					if (sb.length() > 0) {
						return createToken(sb.toString(), readLength);
					}
					this.preReadTokens.add(Token.EOF);
					return Token.EOF;
				case SPACE:
					if (!isOpen && sb.length() > 0) {
						return createToken(sb.toString(), readLength);
					} else if (isOpen) {
						break;
					} else {
						this.currentTokensStart++;
						readLength = 0;
						continue;
					}
				default:
					if (isOperator && sb.length() > 0) {
						this.preRead = r;
						return createToken(sb.toString(), readLength);
					} else {
						isIdentifier = true;
					}
				}
				sb.append((char) r);
			} catch (IOException e) {
				try {
					if (this.reader != null) {
						this.reader.close();
					}
				} catch (IOException e1) {
				}
				this.reader = null;
			}
		}
	}

	protected Token createToken(String str, int readLength) {
		Token t = Token.create(str, this.currentTokensStart, resolveTokenType(str));
		this.currentTokensStart += readLength;
		return t;
	}

	protected TokenType resolveTokenType(String str) {
		char c = str.charAt(0);
		TokenType tmpType = TokenType.IDENTIFIER;
		if (c == QUOTE) {
			tmpType = TokenType.STRING;
		} else if ('0' <= c && c <= '9') {
			tmpType = TokenType.NUMBER;
		} else if (in(str, operators)) {
			tmpType = TokenType.OPERATOR;
		} else if (c == ',') {
			tmpType = TokenType.COMMA;
		} else if (c == PAREN_OPEN) {
			tmpType = TokenType.PAREN_OPEN;
		} else if (c == PAREN_CLOSE) {
			tmpType = TokenType.PAREN_CLOSE;
		} else if (c == BRACE_OPEN) {
			tmpType = TokenType.BRACE_OPEN;
		} else if (c == BRACE_CLOSE) {
			tmpType = TokenType.BRACE_CLOSE;
		} else if (c == SEMICOLON) {
			tmpType = TokenType.TERMINATOR;
		} else if (c == LF) {
			tmpType = TokenType.TERMINATOR;
		}
		return tmpType;
	}

	private static boolean in(String str, String[] target) {
		for (String string : target) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public void push(Token token) {
		preReadTokens.add(token);
	}

	public Token[] toArray() {
		List<Token> list = new ArrayList<>();
		while (this.hasNext()) {
			list.add(this.next());
		}
		list.add(Token.EOF);
		return list.toArray(new Token[] {});
	}
}
