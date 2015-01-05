package edu.rosehulman.serg.smellbuster.util;

import java.util.ArrayList;

public class MetricDOMObject implements IMetricDOMObject {

	private IMetricDOMObject metricDomObj;
	
	public MetricDOMObject(){
		this.metricDomObj = new CKJMDOMObject();
	}
	
	@Override
	public void setValueForMetric(String key, double value) {
		this.metricDomObj.setValueForMetric(key, value);
	}

	@Override
	public void setName(String name) {
		this.metricDomObj.setName(name);
	}

	@Override
	public ArrayList<String> getMetricList() {
		return this.metricDomObj.getMetricList();
	}

	@Override
	public String getName() {
		return this.metricDomObj.getName();
	}

	@Override
	public double getValueForMetric(String metric) {
		return this.metricDomObj.getValueForMetric(metric);
	}

	@Override
	public double getAggregate() {
		return this.metricDomObj.getAggregate();
	}

}
