package net.nchelluri.testFirstChallenge.test;

import junit.framework.TestCase;
import net.nchelluri.testFirstChallenge.Sheet;

public class SheetTest extends TestCase {
	public void testThatCellsAreEmptyByDefault() {
		Sheet sheet = new Sheet();
		assertEquals("", sheet.get("A1"));
		assertEquals("", sheet.get("ZX347"));
	}

	public void testThatTextCellsAreStored() {
		Sheet sheet = new Sheet();
		String theCell = "A21";

		sheet.put(theCell, "A string");
		assertEquals("A string", sheet.get(theCell));

		sheet.put(theCell, "A different string");
		assertEquals("A different string", sheet.get(theCell));

		sheet.put(theCell, "");
		assertEquals("", sheet.get(theCell));
	}

	public void testThatManyCellsExist() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "First");
		sheet.put("X27", "Second");
		sheet.put("ZX901", "Third");

		assertEquals("A1", "First", sheet.get("A1"));
		assertEquals("X27", "Second", sheet.get("X27"));
		assertEquals("ZX901", "Third", sheet.get("ZX901"));

		sheet.put("A1", "Fourth");
		assertEquals("A1 after", "Fourth", sheet.get("A1"));
		assertEquals("X27 same", "Second", sheet.get("X27"));
		assertEquals("ZX901 same", "Third", sheet.get("ZX901"));
	}

	public void testThatNumericCellsAreIdentifiedAndStored() {
		Sheet sheet = new Sheet();
		String theCell = "A21";

		sheet.put(theCell, "X99"); // "Obvious" string
		assertEquals("X99", sheet.get(theCell));

		sheet.put(theCell, "14"); // "Obvious" number
		assertEquals("14", sheet.get(theCell));

		sheet.put(theCell, " 99 X"); // Whole string must be numeric
		assertEquals(" 99 X", sheet.get(theCell));

		sheet.put(theCell, " 1234 "); // Blanks ignored
		assertEquals("1234", sheet.get(theCell));

		sheet.put(theCell, " "); // Just a blank
		assertEquals(" ", sheet.get(theCell));
	}

	public void testThatWeHaveAccessToCellLiteralValuesForEditing() {
		Sheet sheet = new Sheet();
		String theCell = "A21";

		sheet.put(theCell, "Some string");
		assertEquals("Some string", sheet.getLiteral(theCell));

		sheet.put(theCell, " 1234 ");
		assertEquals(" 1234 ", sheet.getLiteral(theCell));

		sheet.put(theCell, "=7"); // Foreshadowing formulas:)
		assertEquals("=7", sheet.getLiteral(theCell));
	}

	public void testFormulaSpec() {
		Sheet sheet = new Sheet();
		sheet.put("B1", " =7"); // note leading space
		assertEquals("Not a formula", " =7", sheet.get("B1"));
		assertEquals("Unchanged", " =7", sheet.getLiteral("B1"));
	}

	public void testConstantFormula() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=7");
		assertEquals("Formula", "=7", sheet.getLiteral("A1"));
		assertEquals("Value", "7", sheet.get("A1"));
	}

	public void testParentheses() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=(7)");
		assertEquals("Parends", "7", sheet.get("A1"));
	}

	public void testDeepParentheses() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=((((10))))");
		assertEquals("Parends", "10", sheet.get("A1"));
	}

	public void testMultiply() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=2*3*4");
		assertEquals("Times", "24", sheet.get("A1"));
	}

	public void testAdd() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=71+2+3");
		assertEquals("Add", "76", sheet.get("A1"));
	}

	public void testPrecedence() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=7+2*3");
		assertEquals("Precedence", "13", sheet.get("A1"));
	}

	public void testFullExpression() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=7*(2+3)*((((2+1))))");
		assertEquals("Expr", "105", sheet.get("A1"));
	}

	public void testSimpleFormulaError() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=7*");
		assertEquals("Error", "#Error", sheet.get("A1"));
	}

	public void testParenthesisError() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=(((((7))");
		assertEquals("Error", "#Error", sheet.get("A1"));
	}

	public void testDivide() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=24/3");
		assertEquals("Divide", "8", sheet.get("A1"));
	}

	public void testDivideByZero() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=1/0");
		assertEquals("Error", "#DivideByZero", sheet.get("A1"));
	}

	public void testThatCellReferenceWorks() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "8");
		sheet.put("A2", "=A1");
		assertEquals("cell lookup", "8", sheet.get("A2"));
	}

	public void testThatCellChangesPropagate() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "8");
		sheet.put("A2", "=A1");
		assertEquals("cell lookup", "8", sheet.get("A2"));

		sheet.put("A1", "9");
		assertEquals("cell change propagation", "9", sheet.get("A2"));
	}

	public void testThatFormulasKnowCellsAndRecalculate() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "8");
		sheet.put("A2", "3");
		sheet.put("B1", "=A1*(A1-A2)+A2/3");
		assertEquals("calculation with cells", "41", sheet.get("B1"));

		sheet.put("A2", "6");
		assertEquals("re-calculation", "18", sheet.get("B1"));
	}

	public void testThatDeepPropagationWorks() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "8");
		sheet.put("A2", "=A1");
		sheet.put("A3", "=A2");
		sheet.put("A4", "=A3");
		assertEquals("deep propagation", "8", sheet.get("A4"));

		sheet.put("A2", "6");
		assertEquals("deep re-calculation", "6", sheet.get("A4"));
	}

	public void testThatFormulaWorksWithManyCells() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "10");
		sheet.put("A2", "=A1+B1");
		sheet.put("A3", "=A2+B2");
		sheet.put("A4", "=A3");
		sheet.put("B1", "7");
		sheet.put("B2", "=A2");
		sheet.put("B3", "=A3-A2");
		sheet.put("B4", "=A4+B3");

		assertEquals("multiple expressions - A4", "34", sheet.get("A4"));
		assertEquals("multiple expressions - B4", "51", sheet.get("B4"));
	}

	public void testThatCircularReferenceDoesntCrash() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=A1");
		assertTrue(true);
	}

	public void testThatCircularReferencesAdmitIt() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=A1");
		assertEquals("Detect circularity", "#Circular", sheet.get("A1"));
	}

	public void testThatDeepCircularReferencesAdmitIt() {
		Sheet sheet = new Sheet();
		sheet.put("A1", "=A2");
		sheet.put("A2", "=A3");
		sheet.put("A3", "=A1");
		assertEquals("Detect deep circularity", "#Circular", sheet.get("A1"));

		sheet.put("A1", "=A2");
		sheet.put("A2", "=A3");
		sheet.put("A3", "=B1 + 24");
		sheet.put("B1", "=43 + A1");
		assertEquals("Detect deep circularity", "#Circular", sheet.get("A1"));
	}
}
