package edu.rosehulman.serg.smellbuster.logic;

import java.awt.Color;

public interface IMetricLogic {

	public double getMaxAggregateValue();
	public void setMaxAggregateValue(double value);
	public Color getColorForMetricScore(int index, String className,
			String version);
	public String getAggregateMetricsOfMin(String className);
	public String getAggregateMetricsOfMax(String className);
	public String getAggregateMetrics(String className);
}
