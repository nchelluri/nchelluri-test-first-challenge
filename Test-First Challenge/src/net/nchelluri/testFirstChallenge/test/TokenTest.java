package net.nchelluri.testFirstChallenge.test;

import junit.framework.TestCase;
import net.nchelluri.testFirstChallenge.Token;

public class TokenTest extends TestCase {
	public void testIsOperator() {
		assertTrue(Token.fromString("*").isOperator());
		assertTrue(Token.fromString("/").isOperator());
		assertTrue(Token.fromString("+").isOperator());
		assertTrue(Token.fromString("-").isOperator());

		assertFalse(Token.fromString("(").isOperator());
		assertFalse(Token.fromString(")").isOperator());
		assertFalse(Token.fromString("3").isOperator());

		assertTrue(Token.fromString("+").isPlus());
		assertFalse(Token.fromString("-").isPlus());
		assertFalse(Token.fromString("*").isPlus());
		assertFalse(Token.fromString("/").isPlus());

		assertTrue(Token.fromString("-").isMinus());
		assertFalse(Token.fromString("+").isMinus());
		assertFalse(Token.fromString("*").isMinus());
		assertFalse(Token.fromString("/").isMinus());

		assertTrue(Token.fromString("*").isTimes());
		assertFalse(Token.fromString("-").isTimes());
		assertFalse(Token.fromString("+").isTimes());
		assertFalse(Token.fromString("/").isTimes());

		assertTrue(Token.fromString("/").isDivides());
		assertFalse(Token.fromString("*").isDivides());
		assertFalse(Token.fromString("+").isDivides());
		assertFalse(Token.fromString("-").isDivides());
	}

	public void testIsNum() {
		assertTrue(Token.fromString("5").isNum());
		assertFalse(Token.fromString("+").isNum());
	}

	public void testPrecedence() {
		assertEquals(2, Token.fromString("*").precedence());
		assertEquals(2, Token.fromString("/").precedence());
		assertEquals(1, Token.fromString("+").precedence());
		assertEquals(1, Token.fromString("-").precedence());
		assertEquals(0, Token.fromString("(").precedence());
		assertEquals(0, Token.fromString(")").precedence());
	}

	public void testIsParen() {
		assertTrue(Token.fromString("(").isParen());
		assertTrue(Token.fromString("(").isParen());
		assertFalse(Token.fromString("+").isParen());
		assertFalse(Token.fromString("7").isParen());

		assertTrue(Token.fromString("(").isLeftParen());
		assertFalse(Token.fromString(")").isLeftParen());
		assertFalse(Token.fromString("+").isLeftParen());
		assertFalse(Token.fromString("7").isLeftParen());

		assertTrue(Token.fromString(")").isRightParen());
		assertFalse(Token.fromString("(").isRightParen());
		assertFalse(Token.fromString("+").isRightParen());
		assertFalse(Token.fromString("7").isRightParen());
	}

	public void testFromIntValue() {
		assertEquals(3, Token.fromInt(3).intValue());
	}

	public void testIsCellName() {
		assertTrue(Token.fromString("A1").isCellName());
		assertFalse(Token.fromString("3A").isCellName());
		assertFalse(Token.fromString("A").isCellName());
		assertFalse(Token.fromString("3").isCellName());

		assertTrue(Token.fromString("A1").isCellName("A1"));
		assertTrue(Token.fromString("B2").isCellName("B2"));
		assertFalse(Token.fromString("A1").isCellName("B2"));
		assertFalse(Token.fromString("A").isCellName("A"));
	}

	public void testCellName() {
		assertEquals("A1", Token.fromString("A1").cellName());
		assertEquals(null, Token.fromString("A").cellName());
	}
}
