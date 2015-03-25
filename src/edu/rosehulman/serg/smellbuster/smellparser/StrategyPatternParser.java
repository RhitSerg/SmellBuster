package edu.rosehulman.serg.smellbuster.smellparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class StrategyPatternParser {

	int i = 0;
	private Map<String, List<ASTNode>> methodIfStatementsMap;
	private Map<ASTNode, List<ASTNode>> ifStatmentParentMap;
	private ArrayList<String> methodNames;

	public StrategyPatternParser() {
		this.methodIfStatementsMap = new HashMap<>();
		this.ifStatmentParentMap = new HashMap<>();
		this.methodNames = new ArrayList<>();
	}

	public void parseFile(String filePath) throws IOException {
		File file = new File(filePath);
		String fileToString = FileUtils.readFileToString(file);
		parse(fileToString);
	}

	// use ASTParse to parse string
	public void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {

			public boolean visit(MethodDeclaration method) {
				// System.out.println("method: "
				// + method.getName().getFullyQualifiedName());
				// System.out.println("------------------");

				Block methodBlock = method.getBody();
				if (methodBlock != null) {
					String myblock = methodBlock.toString();
					methodVisitor(myblock, method.getName()
							.getFullyQualifiedName());
				}
				return false;
			}
		});

	}

	public void methodVisitor(String content, final String methodName) {

		ASTParser metparse = ASTParser.newParser(AST.JLS3);
		metparse.setSource(content.toCharArray());
		metparse.setKind(ASTParser.K_STATEMENTS);
		Block block = (Block) metparse.createAST(null);

		block.accept(new ASTVisitor() {

			public boolean visit(IfStatement ifStatement) {

				List<ASTNode> parents = findParentIf(ifStatement);

				StrategyPatternParser.this.ifStatmentParentMap.put(ifStatement,
						parents);

				List<ASTNode> ifStatements = new ArrayList<>();

				if (StrategyPatternParser.this.methodIfStatementsMap
						.containsKey(methodName)) {
					ifStatements = StrategyPatternParser.this.methodIfStatementsMap
							.get(methodName);
				}

				ifStatements.add(ifStatement);

				StrategyPatternParser.this.methodIfStatementsMap.put(methodName,
						ifStatements);
				StrategyPatternParser.this.methodNames.add(methodName);

				return true;
			}

		});
	}

	public List<ASTNode> findParentIf(IfStatement node) {
		List<ASTNode> parents = new ArrayList<>();

		ASTNode parent = node.getParent();
		while (parent != null) {
			if (parent.getNodeType() == ASTNode.IF_STATEMENT) {
				parents.add(parent);
			}
			parent = parent.getParent();
		}

		return parents;
	}

	public Map<ASTNode, Integer> getNodeToChange(String methodName) {
		List<ASTNode> ifNodes = this.methodIfStatementsMap.get(methodName);

		int max = 0;
		ASTNode nodeToChange = null;
		for (ASTNode astNode : ifNodes) {
			int counter = 0;
			IfStatement ifNode = (IfStatement) astNode;
			while (ifNode != null) {
				counter++;
				ifNode = getElseIfNodeFor(methodName, ifNode);
			}
			if (counter > max) {
				max = counter;
				nodeToChange = astNode;
			}
		}

		Map<ASTNode, Integer> result = new HashMap<>();
		result.put(nodeToChange, max);
		return result;
	}

	private IfStatement getElseIfNodeFor(String methodName, IfStatement ifNode) {
		List<ASTNode> ifNodes = this.methodIfStatementsMap.get(methodName);

		for (ASTNode astNode : ifNodes) {
			Statement elseStatement = ifNode.getElseStatement();
			if (elseStatement == null){
				return null;
			}
			String elseStatementString = elseStatement.toString();
			String astNodeString = astNode.toString();
			if (elseStatementString.equals(astNodeString)) {
				return (IfStatement) astNode;
			}
		}
		return null;
	}

	public Map<String, List<ASTNode>> getMethodIfStatementsMap() {
		return this.methodIfStatementsMap;
	}
	
	public Map<ASTNode, List<ASTNode>> getIfStatementParentsMap(){
		return this.ifStatmentParentMap;
	}

	public ArrayList<String> getMethodNames() {
		return this.methodNames;
	}
}
