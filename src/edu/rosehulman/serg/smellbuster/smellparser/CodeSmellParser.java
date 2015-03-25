package edu.rosehulman.serg.smellbuster.smellparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

public class CodeSmellParser implements ISmellParser {

	@Override
	public Map<String, List<String>> getCodeSmellsForFile(String filePath) {

		List<String> strategyPatternImprovement = this
				.getCodeForStrategyPattern(filePath);

		Map<String, List<String>> designPatternMap = new HashMap<>();
		designPatternMap.put("Strategy", strategyPatternImprovement);

		return designPatternMap;
	}

	private List<String> getCodeForStrategyPattern(String filePath) {
		StrategyPatternParser parser = new StrategyPatternParser();
		List<String> codeSnippetsToChange = new ArrayList<>();
		try {
			parser.parseFile(filePath);
			ArrayList<String> methodNames = parser.getMethodNames();
			for (String method : methodNames) {
				Map<ASTNode, Integer> codeToChange = parser
						.getNodeToChange(method);
				ASTNode node = codeToChange.keySet().iterator().next();
				if (node != null) {
					int patternScore = codeToChange.get(node);
					if (patternScore >= 3) {
						codeSnippetsToChange.add(node.toString());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return codeSnippetsToChange;
	}

}
