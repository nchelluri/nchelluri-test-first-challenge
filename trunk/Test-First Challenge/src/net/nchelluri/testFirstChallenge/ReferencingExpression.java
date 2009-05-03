package net.nchelluri.testFirstChallenge;

import java.util.Collection;
import java.util.Stack;
import java.util.StringTokenizer;

import net.nchelluri.testFirstChallenge.exception.CircularReferenceException;

public class ReferencingExpression {
	private final Stack<Token> operands = new Stack<Token>();
	private final Sheet referencedSheet;
	private final Collection<String> referencingCellNames;
	private final String postfix;

	private ReferencingExpression(Sheet referencedSheet,
			Collection<String> referencingCellNames, String postfix) {
		this.referencedSheet = referencedSheet;
		this.referencingCellNames = referencingCellNames;
		this.postfix = postfix;
	}

	public static ReferencingExpression from(Sheet referencedSheet,
			Collection<String> referencingCellNames, String infix)
			throws Exception {
		String postfix = InfixToPostfixConverter.convert(infix);
		return new ReferencingExpression(referencedSheet, referencingCellNames,
				postfix);
	}

	public String evaluate() throws Exception {
		StringTokenizer tokenizer = new StringTokenizer(postfix, "*/+- ", true);

		while (tokenizer.hasMoreTokens()) {
			Token token = Token.fromString(tokenizer.nextToken());
			evaluateToken(token);
		}

		return operands.pop().toString();
	}

	private void evaluateToken(Token token) throws Exception {
		if (token.isNum()) {
			operands.push(token);
		} else if (token.isOperator()) {
			operands.push(perform(token));
		} else if (token.isCellName()) {
			operands.push(dereference(token));
		}
	}

	private Token perform(Token operator) {
		Token operand2 = operands.pop();
		Token operand1 = operands.pop();

		return doOp(operand1, operator, operand2);
	}

	private Token dereference(Token reference) throws Exception {
		if (referencingCellNames.contains(reference.cellName())) {
			throw new CircularReferenceException();
		}

		String result = referencedSheet.get(reference.cellName(),
				referencingCellNames);

		if ("#Circular".equals(result)) {
			throw new CircularReferenceException();
		}
		return Token.fromString(result);
	}

	private static Token doOp(Token operand1, Token operator, Token operand2) {
		int x = operand1.intValue();
		int y = operand2.intValue();

		if (operator.isTimes()) {
			return Token.fromInt(x * y);
		}

		if (operator.isDivides()) {
			return Token.fromInt(x / y);
		}

		if (operator.isPlus()) {
			return Token.fromInt(x + y);
		}

		if (operator.isMinus()) {
			return Token.fromInt(x - y);
		}

		return null;
	}
}
