package lexer;

public class Str extends Token {
	public final String value;

	public Str(StringBuffer v) {
		super(Tag.LITERAL);
		value = v.toString();
	}

	public String toString() {
		return value;
	}
}
