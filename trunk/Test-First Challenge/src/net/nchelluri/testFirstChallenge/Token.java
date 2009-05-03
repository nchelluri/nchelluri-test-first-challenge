package net.nchelluri.testFirstChallenge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
	private static final Pattern numeric = Pattern.compile("\\d+");
	private static final Pattern cellName = Pattern.compile("[A-Z]+\\d+");
	private final String value;

	private Token(String value) {
		this.value = value;
	}

	public static Token fromString(String input) {
		return new Token(input);
	}

	public static Token fromInt(int input) {
		return new Token("" + input);
	}

	public boolean isOperator() {
		if (isTimes() || isDivides() || isPlus() || isMinus()) {
			return true;
		}

		return false;
	}

	public boolean isNum() {
		Matcher matcher = numeric.matcher(value);
		if (matcher.matches()) {
			return true;
		}

		return false;
	}

	public boolean isParen() {
		return isLeftParen() || isRightParen();
	}

	public boolean isLeftParen() {
		return "(".equals(value);
	}

	public boolean isRightParen() {
		return ")".equals(value);
	}

	public String toString() {
		return value;
	}

	public int precedence() {
		if (isTimes() || isDivides()) {
			return 2;
		}

		if (isPlus() || isMinus()) {
			return 1;
		}

		return 0;
	}

	public boolean isCellName() {
		Matcher matcher = cellName.matcher(value);
		if (matcher.matches()) {
			return true;
		}

		return false;
	}

	public int intValue() {
		return Integer.parseInt(value);
	}

	public boolean isTimes() {
		return "*".equals(value);
	}

	public boolean isDivides() {
		return "/".equals(value);
	}

	public boolean isPlus() {
		return "+".equals(value);
	}

	public boolean isMinus() {
		return "-".equals(value);
	}

	public boolean isCellName(String cellName) {
		return isCellName() && value.equals(cellName);
	}

	public String cellName() {
		if (isCellName()) {
			return value;
		}

		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Token))
			return false;
		final Token other = (Token) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
