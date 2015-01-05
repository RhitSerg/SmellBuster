package edu.rosehulman.serg.smellbuster.logic;

import java.awt.Color;

public class MetricLogic implements IMetricLogic {

	private IMetricLogic metricLogic;
	
	public MetricLogic(ResultTableLogic resultLogic){
		this.metricLogic = new CKJMMetricLogic(resultLogic);
	}
	
	@Override
	public double getMaxAggregateValue() {
		return this.metricLogic.getMaxAggregateValue();
	}

	@Override
	public void setMaxAggregateValue(double value) {
		this.metricLogic.setMaxAggregateValue(value);
	}

	@Override
	public Color getColorForMetricScore(int index, String className,
			String version) {
		return this.metricLogic.getColorForMetricScore(index, className, version);
	}

	@Override
	public String getAggregateMetricsOfMin(String className) {
		return this.metricLogic.getAggregateMetricsOfMin(className);
	}

	@Override
	public String getAggregateMetricsOfMax(String className) {
		return this.metricLogic.getAggregateMetricsOfMax(className);
	}

	@Override
	public String getAggregateMetrics(String className) {
		return this.getAggregateMetrics(className);
	}

}
