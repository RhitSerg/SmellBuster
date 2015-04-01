package edu.rosehulman.serg.smellbuster.smellparser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
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
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class PatternParser {

	private Map<String, List<ASTNode>> methodIfStatementsMap;
	private Map<String, Integer> methodParameterListMap;
	private Map<String, String> methodSignatureMap;
	private Map<ASTNode, List<ASTNode>> ifStatmentParentMap;
	private ArrayList<String> lineCommentList;
	private ArrayList<String> blockCommentList;
	private ArrayList<String> methodNames;
	private String fileContent;

	public PatternParser() {
		this.methodIfStatementsMap = new HashMap<>();
		this.ifStatmentParentMap = new HashMap<>();
		this.methodSignatureMap = new HashMap<>();
		this.methodParameterListMap = new HashMap<>();
		this.methodNames = new ArrayList<>();
		this.lineCommentList = new ArrayList<>();
		this.blockCommentList = new ArrayList<>();
		this.fileContent = "";
	}

	public void parseFile(String filePath) throws IOException {
		File file = new File(filePath);
		String fileToString = FileUtils.readFileToString(file);
		this.fileContent = fileToString;
		parse(fileToString);
	}

	@SuppressWarnings("unchecked")
	public void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {

			public boolean visit(MethodDeclaration method) {

				Block methodBlock = method.getBody();
				if (methodBlock != null) {
					String myblock = methodBlock.toString();
					String methodName = method.getName()
							.getFullyQualifiedName();
					PatternParser.this.methodNames.add(methodName);

					String fullMethodSignature = getMethodSignature(method)
							.toString();
					PatternParser.this.methodSignatureMap.put(methodName,
							fullMethodSignature);

					List<String> methodParameters = method.parameters();
					PatternParser.this.methodParameterListMap.put(methodName,
							methodParameters.size());

					methodVisitor(myblock, methodName);

				}
				return false;
			}
		});
		
		for (Comment comment : (List<Comment>) cu.getCommentList()) {
			int start = comment.getStartPosition();
			int end = start + comment.getLength();
			String commentString = this.fileContent.substring(start, end);
			if (comment.isBlockComment()){
				this.blockCommentList.add(commentString);
			}
			else if (comment.isLineComment()){
				this.lineCommentList.add(commentString);
			}
		}

	}

	public void methodVisitor(String content, final String methodName) {

		ASTParser metparse = ASTParser.newParser(AST.JLS3);
		metparse.setSource(content.toCharArray());
		metparse.setKind(ASTParser.K_STATEMENTS);
		Block block = (Block) metparse.createAST(null);

		block.accept(new ASTVisitor() {

			public boolean visit(IfStatement ifStatement) {

				List<ASTNode> parents = findParentIf(ifStatement);

				PatternParser.this.ifStatmentParentMap
						.put(ifStatement, parents);

				List<ASTNode> ifStatements = new ArrayList<>();

				if (PatternParser.this.methodIfStatementsMap
						.containsKey(methodName)) {
					ifStatements = PatternParser.this.methodIfStatementsMap
							.get(methodName);
				}

				ifStatements.add(ifStatement);

				PatternParser.this.methodIfStatementsMap.put(methodName,
						ifStatements);

				return true;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private StringBuilder getMethodSignature(MethodDeclaration method) {
		StringBuilder name = new StringBuilder();
		name.append(Modifier.toString(method.getModifiers()));
		name.append(" ");
		name.append(method.getReturnType2());
		name.append(" ");
		name.append(method.getName().getFullyQualifiedName());
		name.append("(");

		String comma = "";

		List<SingleVariableDeclaration> parameterNames = method.parameters();
		for (SingleVariableDeclaration parameter : parameterNames) {
			name.append(comma);
			name.append(parameter.toString());
			comma = ", ";
		}

		name.append(")");
		return name;
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

	public Map<ASTNode, Integer> getNodeToChangeForStrategy(String methodName) {
		List<ASTNode> ifNodes = this.methodIfStatementsMap.get(methodName);
		Map<ASTNode, Integer> result = new HashMap<>();
		int max = 2;
		if (ifNodes != null) {
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

			result.put(nodeToChange, max);
		}
		return result;
	}

	private IfStatement getElseIfNodeFor(String methodName, IfStatement ifNode) {
		List<ASTNode> ifNodes = this.methodIfStatementsMap.get(methodName);

		for (ASTNode astNode : ifNodes) {
			Statement elseStatement = ifNode.getElseStatement();
			if (elseStatement == null) {
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

	public Map<ASTNode, List<ASTNode>> getIfStatementParentsMap() {
		return this.ifStatmentParentMap;
	}

	public ArrayList<String> getMethodNames() {
		return this.methodNames;
	}

	public String getFileContents() {
		return this.fileContent;
	}

	public String getNodeToChangeForParameterList(String methodName) {
		int numParameters = this.methodParameterListMap.get(methodName);
		if (numParameters > 3) {
			return this.methodSignatureMap.get(methodName);
		}
		return "";
	}
	
	public ArrayList<String> getNodeToChangeForComments(){
		ArrayList<String> commentList = new ArrayList<>(this.lineCommentList);
		double numBlockComments = this.blockCommentList.size();
		double methods = this.methodNames.size();
		double commentToMethodRatio = numBlockComments / methods;
		if (commentToMethodRatio > 0.2){
			commentList.addAll(this.blockCommentList);
		}
		return commentList;
	}
}
