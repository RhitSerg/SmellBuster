package edu.rosehulman.serg.smellbuster.smellparser;

import java.util.List;
import java.util.Map;

public interface ISmellParser {

	public Map<String, List<String>> getCodeSmellsForFile(String filePath);
	
}
