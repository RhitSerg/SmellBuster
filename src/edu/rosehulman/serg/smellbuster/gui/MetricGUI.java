package edu.rosehulman.serg.smellbuster.gui;

import edu.rosehulman.serg.smellbuster.logic.ResultTableLogic;

public class MetricGUI implements IMetricGUI {
	
	IMetricGUI metricGUI;
	
	public MetricGUI(ResultTableLogic displayTableLogic){
		this.metricGUI = new CKJMGui(displayTableLogic);
	}

	@Override
	public String[] getMetricDisplayList() {
		return this.metricGUI.getMetricDisplayList();
	}

	@Override
	public String getDisplayMessage(String version, String className) {
		return this.metricGUI.getDisplayMessage(version, className);
	}

}
