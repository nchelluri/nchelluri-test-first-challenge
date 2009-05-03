package net.nchelluri.testFirstChallenge.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUnitTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("All Unit Tests");
		suite.addTestSuite(TokenTest.class);
		suite.addTestSuite(InfixToPostfixConverterTest.class);
		suite.addTestSuite(SheetTest.class);

		return suite;
	}
}
