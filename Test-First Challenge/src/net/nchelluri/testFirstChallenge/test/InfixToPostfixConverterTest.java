package net.nchelluri.testFirstChallenge.test;

import junit.framework.TestCase;
import net.nchelluri.testFirstChallenge.InfixToPostfixConverter;

public class InfixToPostfixConverterTest extends TestCase {
	public void testConvertToPostfix() throws Exception {
		assertEquals("2 3 * 4 5 / -", InfixToPostfixConverter
				.convert("2*3-4/5"));
		assertEquals("15 3 7 + * 6 -", InfixToPostfixConverter
				.convert("15 * (3 + 7) - 6"));
		assertEquals("95 3 + 7 / 8 *", InfixToPostfixConverter
				.convert("((95 + 3) / 7) * 8"));
	}
}
