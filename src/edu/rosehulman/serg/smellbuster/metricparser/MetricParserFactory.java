package edu.rosehulman.serg.smellbuster.metricparser;

import java.util.ArrayList;

import edu.rosehulman.serg.smellbuster.util.DiffClass;
import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public class MetricParserFactory implements IMetricParser {

	private IMetricParser metricParser;
	
	public MetricParserFactory(){
		this.metricParser = new CKJMParser();
	}

	@Override
	public void initializeDOMFromFile(String fileName) {
		this.metricParser.initializeDOMFromFile(fileName);
	}

	@Override
	public void parseXML() {
		this.metricParser.parseXML();
	}

	@Override
	public ArrayList<MetricDOMObject> getDOMObjList() {
		return this.metricParser.getDOMObjList();
	}

	@Override
	public ArrayList<DiffClass> getDiffClassList() {
		return this.metricParser.getDiffClassList();
	}

	@Override
	public ArrayList<String> getBaseVersion() {
		return this.metricParser.getBaseVersion();
	}
	
}
