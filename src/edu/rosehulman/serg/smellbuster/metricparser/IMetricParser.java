package edu.rosehulman.serg.smellbuster.metricparser;

import java.util.ArrayList;

import edu.rosehulman.serg.smellbuster.util.DiffClass;
import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public interface IMetricParser {

	public void initializeDOMFromFile(String fileName);
	public void parseXML();
	public ArrayList<MetricDOMObject> getDOMObjList();
	public ArrayList<DiffClass> getDiffClassList();
	public ArrayList<String> getBaseVersion();
}
