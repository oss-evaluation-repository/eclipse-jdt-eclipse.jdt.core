/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.compiler.regression;

import org.eclipse.jdt.core.tests.junit.extension.TestCase;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

import junit.framework.Test;

// Bug 488662 - [1.9] Allow private methods in interfaces 
@SuppressWarnings({ "rawtypes" })
public class InterfaceMethodsTest_9 extends AbstractComparableTest {

// Static initializer to specify tests subset using TESTS_* static variables
// All specified tests which do not belong to the class are skipped...
	static {
//			TESTS_NAMES = new String[] { "testBug488662_001" };
//			TESTS_NUMBERS = new int[] { 561 };
//			TESTS_RANGE = new int[] { 1, 2049 };
	}

	public static Test suite() {
		return buildMinimalComplianceTestSuite(testClass(), F_9);
	}

	public static Test setUpTest(Test test) throws Exception {
		TestCase.setUpTest(test);
		RegressionTestSetup suite = new RegressionTestSetup(ClassFileConstants.JDK9);
		suite.addTest(test);
		return suite;
	}

	public static Class testClass() {
		return InterfaceMethodsTest_9.class;
	}

	public InterfaceMethodsTest_9(String name) {
		super(name);
	}

	// private method - positive test
	public void testBug488662_001() {
		runConformTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"@SuppressWarnings(\"unused\")\n" +			
			"    private void foo()  {}\n" +
			"}\n",
		}, 
		"");
	}
	// private method legal combination of modifiers - positive test
	public void testBug488662_002() {
		runConformTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"@SuppressWarnings(\"unused\")\n" +			
			"    private static void foo()  {}\n" +
			"}\n",
		}, 
		"");
	}
	// private method legal combination of modifiers - positive test
	public void testBug488662_003() {
		runConformTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"@SuppressWarnings(\"unused\")\n" +			
			"    private strictfp void foo()  {}\n" +
			"}\n",
		}, 
		"");
	}

	// missing method body - negative test
	public void testBug488662_004() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"@SuppressWarnings(\"unused\")\n" +
			"    private void foo();\n" +
			"}\n"
			},
			"----------\n" +
			"1. ERROR in I.java (at line 3)\n" +
			"	private void foo();\n" +
			"	             ^^^^^\n" +
			"This method requires a body instead of a semicolon\n" +
			"----------\n");
	}

	// illegal modifier combination - negative test
	public void testBug488662_005() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"@SuppressWarnings(\"unused\")\n" +			
			"    private default void foo();\n" +
			"}\n"
			},
			"----------\n" + 
			"1. ERROR in I.java (at line 3)\n" + 
			"	private default void foo();\n" + 
			"	                     ^^^^^\n" + 
			"Illegal combination of modifiers for the private interface method foo; additionally only one of static and strictfp is permitted\n" + 
			"----------\n" + 
			"2. ERROR in I.java (at line 3)\n" + 
			"	private default void foo();\n" + 
			"	                     ^^^^^\n" + 
			"This method requires a body instead of a semicolon\n" + 
			"----------\n");
	}
	// illegal modifier combination - negative test
	public void testBug488662_006() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"	private abstract void foo();\n" +
			"}\n"
			},
			"----------\n" +
			"1. ERROR in I.java (at line 2)\n" +
			"	private abstract void foo();\n" +
			"	                      ^^^^^\n" +
			"Illegal combination of modifiers for the private interface method foo; additionally only one of static and strictfp is permitted\n" + 
			"----------\n");
	}

	// illegal modifier combination - negative test
	public void testBug488662_007() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"    private synchronized void foo();\n" +
			"}\n"
			},
			"----------\n" +
			"1. ERROR in I.java (at line 2)\n" + 
			"	private synchronized void foo();\n" + 
			"	                          ^^^^^\n" + 
			"Illegal modifier for the interface method foo; only public, private, abstract, default, static and strictfp are permitted\n" + 
			"----------\n" + 
			"2. ERROR in I.java (at line 2)\n" + 
			"	private synchronized void foo();\n" + 
			"	                          ^^^^^\n" + 
			"This method requires a body instead of a semicolon\n" + 
			"----------\n");
	}

	// reduced visibility modifier - negative test
	public void testBug488662_008() {
		runNegativeTest(
		new String[] {
			"X.java",
			"interface I {\n"+
			"	public default void foo() {}\n"+
			"}\n"+
			"public class X implements I{\n"+
			"@SuppressWarnings(\"unused\")\n" +			
			"@Override\n" +
			"	private void foo() {}\n"+
			"}\n"
			},
			"----------\n" +
			"1. ERROR in X.java (at line 7)\n" +
			"	private void foo() {}\n"+
			"	             ^^^^^\n" +
			"Cannot reduce the visibility of the inherited method from I\n" + 
			"----------\n");
	}


	// No unimplemented method error - positive test
	public void testBug488662_009() {
		runConformTest(
		new String[] {
			"X.java",
			"interface I {\n"+
			"	private  void foo() {\n"+
			"	}\n"+
			"	public default void bar() {\n"+
			"		foo();\n"+
			"	}\n"+
			"}\n"+
			"public class X implements I{\n"+
			"	public static void main(String[] args) {\n"+
			"		new X().bar();\n"+
			"	}\n"+
			"}\n"
		}, 
		"");
	}
	// illegal modifier combination - multiple errors - negative test
	public void testBug488662_010() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"    private public void foo(){}\n" +
			"}\n"
			},
			"----------\n" +
			"1. ERROR in I.java (at line 2)\n" +
			"	private public void foo(){}\n" +
			"	                    ^^^^^\n" +
			"Illegal combination of modifiers for the private interface method foo; additionally only one of static and strictfp is permitted\n" + 
			"----------\n");
	}
	// illegal modifier combination - negative test
	public void testBug488662_011() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"    private protected void foo();\n" +
			"}\n"
			},
			"----------\n" + 
			"1. ERROR in I.java (at line 2)\n" + 
			"	private protected void foo();\n" + 
			"	                       ^^^^^\n" + 
			"Illegal modifier for the interface method foo; only public, private, abstract, default, static and strictfp are permitted\n" + 
			"----------\n" + 
			"2. ERROR in I.java (at line 2)\n" + 
			"	private protected void foo();\n" + 
			"	                       ^^^^^\n" + 
			"This method requires a body instead of a semicolon\n" + 
			"----------\n");
	}
	// illegal modifier combination - multiple errors - negative test
	public void testBug488662_012() {
		runNegativeTest(
		new String[] {
			"I.java",
			"public interface I {\n" +
			"    private private public default protected void foo();\n" +
			"}\n"
			},
			"----------\n" + 
			"1. ERROR in I.java (at line 2)\n" + 
			"	private private public default protected void foo();\n" + 
			"	                                              ^^^^^\n" + 
			"Duplicate modifier for the method foo in type I\n" + 
			"----------\n" + 
			"2. ERROR in I.java (at line 2)\n" + 
			"	private private public default protected void foo();\n" + 
			"	                                              ^^^^^\n" + 
			"Illegal modifier for the interface method foo; only public, private, abstract, default, static and strictfp are permitted\n" + 
			"----------\n" + 
			"3. ERROR in I.java (at line 2)\n" + 
			"	private private public default protected void foo();\n" + 
			"	                                              ^^^^^\n" + 
			"This method requires a body instead of a semicolon\n" + 
			"----------\n");
	}
}