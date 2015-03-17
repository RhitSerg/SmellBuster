package edu.rosehulman.serg.smellbuster.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

//import com.github.javaparser.JavaParser;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.Node;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.stmt.IfStmt;
//import com.github.javaparser.ast.stmt.Statement;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTTest {

	// public static void main(String[] args) throws Exception {
	// // creates an input stream for the file to be parsed
	// FileInputStream in = new
	// FileInputStream("src/edu/rosehulman/serg/smellbuster/util/StatisticsCalculator.java");
	//
	// CompilationUnit cu;
	// try {
	// // parse the file
	// cu = JavaParser.parse(in);
	// } finally {
	// in.close();
	// }
	//
	// // visit and print the methods names
	// new MethodVisitor().visit(cu, null);
	// System.out.println(MethodVisitor.getNumber());
	// }
	//
	// /**
	// * Simple visitor implementation for visiting MethodDeclaration nodes.
	// */
	// private static class MethodVisitor extends VoidVisitorAdapter {
	//
	// @Override
	// public void visit(MethodDeclaration n, Object arg) {
	// // here you can access the attributes of the method.
	// // this method will be called for all methods in this
	// // CompilationUnit, including inner class methods
	//
	// System.out.println(n.getName());
	// }
	// }
	//
	// /**
	// * Simple visitor implementation for visiting MethodDeclaration nodes.
	// */
	// private static class IfStmtVisitor extends VoidVisitorAdapter<Void> {
	//
	 static int i = 0;
	//
	// @Override
	// public void visit(IfStmt n, Void arg) {
	// // here you can access the attributes of the method.
	// // this method will be called for all methods in this
	// // CompilationUnit, including inner class methods
	// // List<Statement> statements = n.getBody().getStmts();
	// // Statement st = statements.get(0);
	// // List<Node> node = st.getChildrenNodes();
	// // System.out.println(node.get(0).toString());
	// // System.out.println("===========================");
	// cyclomaticCount(n);
	// }
	//
	// private void cyclomaticCount(IfStmt n)
	// {
	// // one for the if-then
	// i++;
	// Statement elseStmt = n.getElseStmt();
	// if (elseStmt != null)
	// {
	// if ( IfStmt.class.isAssignableFrom(elseStmt.getClass()))
	// {
	// cyclomaticCount((IfStmt) elseStmt);
	// }
	// else
	// {
	// // another for the else
	// i++;
	// }
	// }
	// }
	//
	// public static int getNumber() {
	// return i;
	// }
	// }

	// use ASTParse to parse string
	public static void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		cu.accept(new ASTVisitor() {

//			Set<String> names = new HashSet<>();

//			public boolean visit(VariableDeclarationFragment node) {
//				SimpleName name = node.getName();
//
//				this.names.add(name.getIdentifier());
//				System.out.println("Declaration of '" + name + "' at line"
//						+ cu.getLineNumber(name.getStartPosition()));
//				return false; // do not continue
//			}
//
//			public boolean visit(SimpleName node) {
//				if (this.names.contains(node.getIdentifier())) {
//					System.out.println("Usage of '" + node + "' at line "
//							+ cu.getLineNumber(node.getStartPosition()));
//				}
//				return true;
//			}
			
			public boolean visit(MethodDeclaration method) {
	            System.out.println("method: "+method.getName().getFullyQualifiedName());
	            
//	            List<SingleVariableDeclaration> params = method.parameters();
//
//	            for(SingleVariableDeclaration param: params) {
//	            	System.out.println("param "+param.getName().getFullyQualifiedName());
//	            }

	            Block methodBlock = method.getBody();
	            if (methodBlock != null){
	            	String myblock = methodBlock.toString();
	            	methodVisitor(myblock);
	            }
	            System.out.println("------------------");
	            return false;
	        }
		});

	}
	
	public static void methodVisitor(String content) {
	    
	    ASTParser metparse = ASTParser.newParser(AST.JLS3);
	    metparse.setSource(content.toCharArray());
	    metparse.setKind(ASTParser.K_STATEMENTS);
	    Block block = (Block) metparse.createAST(null);

	    block.accept(new ASTVisitor() {
//	        public boolean visit(VariableDeclarationFragment var) {
//	            debug("met.var", var.getName().getFullyQualifiedName());
//	            return false;
//	        }

//	        public boolean visit(SimpleName node) {
//	            debug("SimpleName node", node.getFullyQualifiedName());
//	            return false;
//	        }
	    	
	        public boolean visit(IfStatement myif) {
	            System.out.println("if.statement: "+myif.toString());
	            i++;
	            return false;
	        }

	    });
	}

	// read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	// loop directory to get file list
	public static void ParseFilesInDir() throws IOException {
		File dirs = new File(".");
		String dirPath = dirs.getCanonicalPath() + File.separator + "src"
				+ File.separator + "edu" + File.separator + "rosehulman"
				+ File.separator + "serg" + File.separator + "smellbuster"
				+ File.separator + "util" + File.separator;

		File root = new File(dirPath);
		// System.out.println(rootDir.listFiles());
		File[] files = root.listFiles();
		String filePath = null;

		for (File f : files) {
			filePath = f.getAbsolutePath();
			if (f.isFile()) {
				System.out.println(f.getName());
				parse(readFileToString(filePath));
				System.out.println("Number of If Statements: "+i);
				i = 0;
				System.out.println("****************************************");
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ParseFilesInDir();
	}
}
