package edu.rosehulman.serg.smellbuster.smellparser;

import java.util.Map;

public interface ISmellParser {

	public Map<String, String> getCodeSmellsForFile(String filePath);
	
}
