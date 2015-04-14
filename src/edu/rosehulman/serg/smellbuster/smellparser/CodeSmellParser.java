package edu.rosehulman.serg.smellbuster.smellparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class CodeSmellParser implements ISmellParser {
	
	private Map<String, String> designPatternMap;
	private String fileContents;

	@Override
	public Map<String, String> getCodeSmellsForFile(String filePath) {

		this. designPatternMap = new HashMap<>();
		this.fileContents = "";
		
		this.getCodeForStrategyPattern(filePath);
	
		return designPatternMap;
	}

	private void getCodeForStrategyPattern(String filePath) {
		PatternParser parser = new PatternParser();
		
		String strategyURL = "http://en.wikipedia.org/wiki/Strategy_pattern";
		String strategyPattern = "<p>Using <a href=\""
				+ strategyURL
				+ "\">Strategy Pattern</a> for the following methods will improve the quality of code:</p><ul>";
		
		String introParameterObjectURL = "http://www.javaworld.com/article/2074935/core-java/too-many-parameters-in-java-methods--part-2--parameters-object.html";
		String introParameterObjectPattern = "<p>Using <a href=\""
				+ introParameterObjectURL
				+ "\">Introducing Parameter Object Pattern</a> for the following methods will improve the quality of code:</p><ul>";
		
		String commentSmell = "<p>Please remove the following comments to improve the quality of the code:</p><ul>";

		try {
			parser.parseFile(filePath);
			ArrayList<String> methodNames = parser.getMethodNames();
			
//			ArrayList<String> startegyPrints = new ArrayList<>();
//			ArrayList<String> methodSignaturePrints = new ArrayList<>();
//			ArrayList<String> commentPrints = new ArrayList<>();
			
			for (String method : methodNames) {

				Map<ASTNode, Integer> codeToChangeForStrategy = parser
						.getNodeToChangeForStrategy(method);
				if (codeToChangeForStrategy.keySet().size() > 0) {
					ASTNode node = codeToChangeForStrategy.keySet().iterator().next();
					if (node != null) {
						strategyPattern += ("<li>" + method + "</li>");
//						startegyPrints.add(method);
					}
				}
				
				String methodSignature = parser.getNodeToChangeForParameterList(method);
				if (methodSignature.length() > 0){
					introParameterObjectPattern += ("<li>"+method+"</li>");
//					methodSignaturePrints.add(method);
				}
			}
			
			ArrayList<String> comments = parser.getNodeToChangeForComments();
			for (String comment: comments){
				commentSmell += ("<li>"+comment+"</li>");
//				commentPrints.add(comment);
			}
			
			this.fileContents = parser.getFileContents();
			
//			System.out.println("Strategy");
//			for (String strategyPrint: startegyPrints){
//				System.out.println(strategyPrint);
//			}
//			
//			System.out.println();
//			System.out.println("Method Signature");
//			for (String methodSig: methodSignaturePrints){
//				System.out.println(methodSig);
//			}
//			
//			System.out.println();
//			System.out.println("Comments");
//			for (String comment: commentPrints){
//				System.out.println(comment);
//			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		strategyPattern += ("</ul>");
		introParameterObjectPattern += ("</ul>");
		commentSmell += ("</ul>");

		this.designPatternMap.put("Strategy", strategyPattern);
		this.designPatternMap.put("IntroduceParameterObject", introParameterObjectPattern);
		this.designPatternMap.put("Comment", commentSmell);
	}
	
	public String getFileContents(){
		return this.fileContents;
	}

}
