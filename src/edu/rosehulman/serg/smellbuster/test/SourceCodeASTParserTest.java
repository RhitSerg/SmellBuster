package edu.rosehulman.serg.smellbuster.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rosehulman.serg.smellbuster.smellparser.StrategyPatternParser;

public class SourceCodeASTParserTest {

	private StrategyPatternParser parser;
	private String[] methodNames;

	@Before
	public void setUp() throws Exception {

		this.methodNames = new String[] { "TestMethod1", "TestMethod2",
				"TestMethod3", "TestMethod4", "TestMethod5", "TestMethod6",
				"TestMethod7", "TestMethod8" };

		File dirs = new File(".");
		String dirPath = dirs.getCanonicalPath() + File.separator + "src"
				+ File.separator + "edu" + File.separator + "rosehulman"
				+ File.separator + "serg" + File.separator + "smellbuster"
				+ File.separator + "test" + File.separator + "Test.java";
		this.parser = new StrategyPatternParser();
		this.parser.parseFile(dirPath);
	}

	@After
	public void tearDown() throws Exception {
		this.parser = null;
	}

	@Test
	public void testMethod1() {
		String methodName = this.methodNames[0];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(1, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod2() {
		String methodName = this.methodNames[1];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(3, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod3() {
		String methodName = this.methodNames[2];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(2, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod4() {
		String methodName = this.methodNames[3];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(2, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod5() {
		String methodName = this.methodNames[4];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(2, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod6() {
		String methodName = this.methodNames[5];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(3, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod7() {
		String methodName = this.methodNames[6];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(3, numOfIfElseIfStatements);
	}

	@Test
	public void testMethod8() {
		String methodName = this.methodNames[7];
		Map<ASTNode, Integer> result = this.parser.getNodeToChange(methodName);
		int numOfIfElseIfStatements = 0;
		for (ASTNode node : result.keySet()) {
			System.out.println(node);
			numOfIfElseIfStatements = result.get(node);
		}
		assertEquals(1, numOfIfElseIfStatements);
	}

}
