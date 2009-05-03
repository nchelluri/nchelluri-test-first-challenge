package net.nchelluri.testFirstChallenge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.nchelluri.testFirstChallenge.exception.CircularReferenceException;

public class Sheet {
	private final Map<String, String> cells = new HashMap<String, String>();

	public void put(String cell, String value) {
		cells.put(cell, value);
	}

	public String get(String cellName) {
		return get(cellName, new ArrayList<String>());
	}

	protected String get(String cellName,
			Collection<String> referencingCellNames) {
		String value = getLiteral(cellName);

		referencingCellNames.add(cellName);

		if (isSpacedNum(value)) {
			value = value.trim();
		} else if (isExpression(value)) {
			String formula = getFormula(value);
			value = evaluate(cellName, referencingCellNames, formula);
		}

		referencingCellNames.remove(cellName);

		return value;
	}

	private String evaluate(String cellName,
			Collection<String> referencingCellNames, String formula) {
		String value;
		try {
			value = ReferencingExpression.from(this, referencingCellNames,
					formula).evaluate();
		} catch (CircularReferenceException e) {
			value = "#Circular";
		} catch (ArithmeticException e) {
			if (e.getMessage().contains("/ by zero")) {
				value = "#DivideByZero";
			} else {
				value = "#Error";
			}
		} catch (Exception e) {
			value = "#Error";
		}
		return value;
	}

	public String getLiteral(String cellName) {
		String value = cells.get(cellName);
		if (value == null) {
			value = "";
		}
		return value;
	}

	private String getFormula(String value) {
		String formula = value.substring(1);
		return formula;
	}

	private static boolean isSpacedNum(String value) {
		return Token.fromString(value.trim()).isNum();
	}

	private boolean isExpression(String value) {
		return value.startsWith("=");
	}
}