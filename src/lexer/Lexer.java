package lexer;

import java.io.IOException;
import java.util.Hashtable;
import java.lang.Math;

public class Lexer {

	public static int line = 1;
	char peek = ' ';
	Hashtable words = new Hashtable();

	void reserve(Word w) {
		words.put(w.lexeme, w);
	}
	public Lexer() {
		reserve(new Word("if", Tag.IF));
		reserve(new Word("else", Tag.ELSE));
		reserve(new Word("while", Tag.WHILE));
		reserve(new Word("do", Tag.DO));
		reserve(new Word("break", Tag.BREAK));

		reserve(new Word("void", Tag.VOID));
		reserve(new Word("int", Tag.INT));
		reserve(new Word("double", Tag.DOUBLE));
		reserve(new Word("bool", Tag.BOOL));
		reserve(new Word("string", Tag.STRING));
		reserve(new Word("class", Tag.CLASS));
		reserve(new Word("null", Tag.NULL));
		reserve(new Word("this", Tag.THIS));
		reserve(new Word("extends", Tag.EXTENDS));
		reserve(new Word("for", Tag.FOR));
		reserve(new Word("return", Tag.RETUEN));
		reserve(new Word("new", Tag.NEW));
		reserve(new Word("NewArray", Tag.NEWARRAY));
		reserve(new Word("Print", Tag.PRINT));
		reserve(new Word("ReadInteger", Tag.READINTEGER));
		reserve(new Word("ReaderLine", Tag.READLINE));
		reserve(new Word("static", Tag.STATIC));

		reserve(Word.True);
		reserve(Word.False);
	}

	public void readch() throws IOException {
		peek = (char) System.in.read();
		
	}

	boolean readch(char c) throws IOException {
		readch();
		if (peek != c) {
			return false;
		}
		peek = ' ';
		return true;
	}

	public Token scan() throws IOException {
		outer:
		for (;; readch()) {
			if (peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n') {
				line += 1;
			} 
			// comment
			else if (peek == '/'){
				readch();
				//single line comment
				if(peek == '/'){
					while(true){
						readch();
						if(peek == '\n'){
							line += 1;
							break;
						}
					}
				}
				//multi-line comment
				else if(peek == '*'){
					while(true){
						readch();
						if(peek == '\n'){
							line += 1;
						}
						else if(peek == '*'){
							readch();
							if(peek == '/'){
								break outer;
							}
						}
					}
				}
				else{
					return new Token('/');
				}
			}
			else {
				break;
			}
		}
		// operators
		switch (peek) {
		case '&':
			if (readch('&'))
				return Word.and;
			else
				return new Token('&');
		case '|':
			if (readch('|'))
				return Word.or;
			else
				return new Token('|');
		case '=':
			if (readch('='))
				return Word.eq;
			else
				return new Token('=');
		case '!':
			if (readch('='))
				return Word.ne;
			else
				return new Token('!');
		case '<':
			if (readch('='))
				return Word.le;
			else
				return new Token('<');
		case '>':
			if (readch('='))
				return Word.ge;
			else
				return new Token('>');

		case '+':
			readch();
			return new Token('+');
		case '-':
			readch();
			return new Token('-');
		case '*':
			readch();
			return new Token('*');
		case '%':
			readch();
			return new Token('%');
		case '\\':
			readch();
			return new Token('\\');
		case ';':
			readch();
			return new Token(';');
		case ',':
			readch();
			return new Token(',');
		case '.':
			readch();
			return new Token('.');
		case '(':
			readch();
			return new Token('(');
		case ')':
			readch();
			return new Token(')');
		case '[':
			readch();
			return new Token('[');
		case ']':
			readch();
			return new Token(']');
		case '{':
			readch();
			return new Token('{');
		case '}':
			readch();
			return new Token('}');

		//invalid character
		case '@':
			readch();
			System.out.println("invalid characters '" + peek + "' at line : "  + line);
			System.exit(0);
		}
		//num & float
		if (Character.isDigit(peek)) {
			int v = 0;
			//hex
			if(peek == '0'){
				readch();
				if(peek == 'X' || peek == 'x'){
					StringBuffer hex_num = new StringBuffer("0x");
					readch();
					do {
						hex_num.append(peek);
						readch();
					} while (Character.isDigit(peek));
					return new Num(Integer.decode(hex_num.toString()));
				}
			}
			do {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));
			if (peek != '.')
				return new Num(v);
			float x = v;
			float d = 10;
			for (;;) {
				readch();
				if(peek == 'E'){
					readch();
					boolean isPositive = true;
					int exp = 0;
					if(peek == '+'){
						//nothing to do here
					}
					else if(peek == '-'){
						isPositive = false;
					}
					else if(!Character.isDigit(peek)){
						//error report
						System.out.println("invalid digit value at line : " + line);
						System.exit(0);
					}
					else{
						do {
							exp = 10 * exp + Character.digit(peek, 10);
							readch();
						} while (Character.isDigit(peek));
					}
					if(isPositive){
						return new Real(x * Math.pow(10, exp));
					}
				}
				else if (!Character.isDigit(peek))
					break;
				x = x + Character.digit(peek, 10) / d;
				d = d * 10;
			}
			return new Real(x);
		}
		// identifier
		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();
			Word w = (Word) words.get(s);
			if (w != null)
				return w;
			w = new Word(s, Tag.ID);
			words.put(s, w);
			return w;
		}
		// string
		if(peek == '"'){
			StringBuffer strBuffer = new StringBuffer("");
			while(true){
				readch();
				//end string
				if(peek == '"'){
					return new Str(strBuffer);
				}
				else if(peek == '\n'){
					//error report
					System.out.println("invalid literal string value at line:" + line);
					System.exit(0);
				}
				else{
					strBuffer.append(peek);
				}
			}
		}
		Token tok = new Token(peek);
		peek = ' ';
		return tok;
	}
	
	public void out() {
		System.out.println(words.size());
		
	}

	public char getPeek() {
		return peek;
	}

	public void setPeek(char peek) {
		this.peek = peek;
	}

}
