package edu.rosehulman.serg.smellbuster.util;

import java.util.ArrayList;

public interface IMetricDOMObject {

	public void setValueForMetric(String key, double value);
	public void setName(String name);
	public String getName();
	public ArrayList<String> getMetricList();
	public double getValueForMetric(String metric);
	public double getAggregate();
}
