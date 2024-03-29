package newlang3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {
	private Map<String, LexicalType> reserved;
	private PushbackReader pr;

	public LexicalAnalyzerImpl(InputStream in) {
		pr = new PushbackReader(new InputStreamReader(in));
		reserved = new HashMap<>();
		for (LexicalType type : LexicalType.values()) {
			reserved.put(type.getNotation(), type);
		}
	}

	@Override
	public LexicalUnit get() throws Exception {
		LexicalType type;
		while(true) {
			String var = "";
			boolean asSymbol = false;
			boolean doubleSymbol = false;
			int count = pr.read();
			char c;

			if (count < 0 || count == 65535) return new LexicalUnit(LexicalType.EOF);
			else c = (char) count;
			if (isSpace(c)) continue;
			if (c == '"') return handleLiteral();
			if (isSymbol(String.valueOf(c))) asSymbol = true;
			if (isContinuousSymbol(String.valueOf(c))) doubleSymbol = true;

			while (true) {
				var += c;
				c = (char) pr.read();

				if (c == '.') {
					LexicalUnit tempLU = handleDoubleNum();
					var += '.'+tempLU.getValue().getSValue();
					return new LexicalUnit(tempLU.getType(), new ValueImpl(var));
				} else type = LexicalType.INTVAL;

				if (isSpace(c)) break;
				if (asSymbol && isDigitOrAlpha(String.valueOf(c))) {
					pr.unread(c);
					break;
				}
				if (!isDigitOrAlpha(String.valueOf(c))) {
					if (doubleSymbol) var += c;
					else pr.unread(c);
					break;
				}
			}

			if(isDigit(var)) return new LexicalUnit(type, new ValueImpl(var));
			else if(isSymbol(var)) return new LexicalUnit(reserved.get(var), new ValueImpl(var));
			else if (isVarName(var)) return  new LexicalUnit(LexicalType.NAME, new ValueImpl(var));
			else continue;
		}
	}

	public LexicalUnit handleDoubleNum() throws IOException {
		String doubleVal = "";
		while(true) {
			int count = pr.read();
			if (count < 0) {
				pr.unread(count);
				break;
			}
			char c = (char) count;
			if (!isDigit(String.valueOf(c))) {
				pr.unread(count);
				break;
			}
			doubleVal += c;
		}

		return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(doubleVal));
	}

	public LexicalUnit handleLiteral() throws IOException {
		String literal = "";
		while (true) {
			int count = pr.read();
			if (count < 0) return new LexicalUnit(LexicalType.EOF);
			char c = (char) count;
			if (c == '"') break;
			literal +=   c;
		}

		return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(literal));
	}

	public boolean isVarName(String str) {
		return Pattern.compile("^[A-Za-z]+[A-Za-z0-9]*$").matcher(str).matches();
	}

	public boolean isDigitOrAlpha(String str) {
		return Pattern.compile("^[A-Za-z0-9]+$").matcher(str).matches();
	}

	public boolean isBreakLine(char ch) {
		if (ch == '\n') return true;
		return false;
	}

	public boolean isDigit(String str) {
		return Pattern.compile("^[0-9\\.]+$").matcher(str).matches();
	}

	private boolean isSymbol(String str) throws IOException {
		if (reserved.containsKey(str)) return true;
		return false;
	}

	private boolean isContinuousSymbol(String str) {
		return Pattern.compile("^[<=>]+$").matcher(str).matches();
	}

	public boolean isSpace(char ch) {
		if (ch == ' ') return true;
		return false;
	}

	@Override
	public LexicalUnit peek() throws Exception { //unncessary
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LexicalUnit peek2() throws Exception { //unncessary
		// TODO Auto-generated method stub
		return null;
	}

}
