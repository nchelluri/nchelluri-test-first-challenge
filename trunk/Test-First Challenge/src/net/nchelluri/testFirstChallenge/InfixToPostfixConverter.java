package net.nchelluri.testFirstChallenge;

import java.util.Stack;
import java.util.StringTokenizer;

public class InfixToPostfixConverter {
	public static String convert(String infix) throws Exception {
		StringBuilder postfix = new StringBuilder();
		Stack<Token> operators = new Stack<Token>();
		StringTokenizer tokenizer = new StringTokenizer(infix, "()*/+-", true);
		int parens = 0;

		while (tokenizer.hasMoreTokens()) {
			parens = processToken(postfix, operators, tokenizer, parens);
		}

		while (!operators.isEmpty()) {
			parens = processEndingOperator(postfix, operators, parens);
		}

		postfix.delete(postfix.length() - 1, postfix.length());

		if (parens != 0) {
			throw new Exception("Unbalanced parenthesis");
		}

		return postfix.toString();
	}

	private static int processEndingOperator(StringBuilder postfix,
			Stack<Token> operators, int parens) {
		Token operator = operators.pop();
		if (!operator.isParen()) {
			postfix.append(operator + " ");
		} else {
			if (operator.isRightParen()) {
				parens--;
			}
		}
		return parens;
	}

	private static int processToken(StringBuilder postfix,
			Stack<Token> operators, StringTokenizer tokenizer, int parens) {
		Token token = Token.fromString(tokenizer.nextToken().trim());

		if (token.isNum() || token.isCellName()) {
			postfix.append(token + " ");
		} else if (token.isLeftParen()) {
			parens++;
			operators.push(token);
		} else if (token.isRightParen()) {
			parens--;
			Token operator = operators.pop();
			while (!operator.isLeftParen()) {
				postfix.append(operator + " ");
				operator = operators.pop();
			}
		} else if (token.isOperator()) {
			parens = processOperator(postfix, operators, parens, token);
		}
		return parens;
	}

	private static int processOperator(StringBuilder postfix,
			Stack<Token> operators, int parens, Token token) {
		boolean processed = false;
		while (!processed) {
			if (operators.empty()) {
				operators.push(token);
				processed = true;
			} else {
				Token topStackOp = operators.peek();
				if (token.precedence() > topStackOp.precedence()) {
					operators.push(token);
					processed = true;
				} else {
					Token operator = operators.pop();
					if (operator.isRightParen()) {
						parens--;
					}
					if (!operator.isParen()) {
						postfix.append(operator + " ");
					}
				}
			}
		}
		return parens;
	}
}
