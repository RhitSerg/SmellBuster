package edu.rosehulman.serg.smellbuster.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public class CKJMMetricLogic implements IMetricLogic {

	private double maxAggregateValue;
	private Map<String, ArrayList<MetricDOMObject>> metricMap;
	private ResultTableLogic resultLogic;

	public CKJMMetricLogic(ResultTableLogic resultLogic) {
		this.resultLogic = resultLogic;
		this.metricMap = resultLogic.getMetricMap();
		this.initMaxMetricValue();
	}

	private void initMaxMetricValue() {
		this.maxAggregateValue = Integer.MIN_VALUE;
		for (String ver : metricMap.keySet()) {
			for (MetricDOMObject metricDomObj : metricMap.get(ver)) {
				String name = metricDomObj.getName();
				if (name != null && name.length() > 0) {
					double value = Double.parseDouble(this
							.getAggregateMetrics(name));
					this.maxAggregateValue = Math.max(this.maxAggregateValue,
							value);
					break;
				}
			}
		}
	}

	@Override
	public String getAggregateMetrics(String className) {
		String value = "N/A";
		double score = 0.0;
		ArrayList<MetricDOMObject> classList = this.resultLogic
				.createClassListMap(className);
		for (MetricDOMObject metricDomObj : classList) {
			if (metricDomObj != null) {
				double wmc = metricDomObj.getValueForMetric("wmc");
				double noc = metricDomObj.getValueForMetric("noc");
				double cbo = metricDomObj.getValueForMetric("cbo");
				double lcom3 = metricDomObj.getValueForMetric("lcom3");
				double cam = metricDomObj.getValueForMetric("cam");
				double ic = metricDomObj.getValueForMetric("ic");
				double cbm = metricDomObj.getValueForMetric("cbm");
				double amc = metricDomObj.getValueForMetric("amc");
				double cc = metricDomObj.getValueForMetric("cc");
				score += ((10 - noc) - wmc - cbo - lcom3 + (2 * cam) - ic - cbm
						- (0.5 * amc) - cc);
			}
		}
		value = String.valueOf(score);
		return value;
	}

	@Override
	public String getAggregateMetricsOfMax(String className) {
		String value = "N/A";
		ArrayList<MetricDOMObject> classList = this.resultLogic
				.createClassListMap(className);
		double score = Integer.MIN_VALUE;
		for (MetricDOMObject metricDomObj : classList) {
			if (metricDomObj != null) {
				double wmc = metricDomObj.getValueForMetric("wmc");
				double noc = metricDomObj.getValueForMetric("noc");
				double cbo = metricDomObj.getValueForMetric("cbo");
				double lcom3 = metricDomObj.getValueForMetric("lcom3");
				double cam = metricDomObj.getValueForMetric("cam");
				double ic = metricDomObj.getValueForMetric("ic");
				double cbm = metricDomObj.getValueForMetric("cbm");
				double amc = metricDomObj.getValueForMetric("amc");
				double cc = metricDomObj.getValueForMetric("cc");

				score = Math.max(score, ((10 - noc) - wmc - cbo - lcom3
						+ (2 * cam) - ic - cbm - (0.5 * amc) - cc));
			}
		}
		value = String.valueOf(score);
		return value;
	}

	@Override
	public String getAggregateMetricsOfMin(String className) {
		String value = "N/A";
		ArrayList<MetricDOMObject> classList = this.resultLogic
				.createClassListMap(className);
		double score = Integer.MAX_VALUE;
		for (MetricDOMObject metricDomObj : classList) {
			if (metricDomObj != null) {
				double wmc = metricDomObj.getValueForMetric("wmc");
				double noc = metricDomObj.getValueForMetric("noc");
				double cbo = metricDomObj.getValueForMetric("cbo");
				double lcom3 = metricDomObj.getValueForMetric("lcom3");
				double cam = metricDomObj.getValueForMetric("cam");
				double ic = metricDomObj.getValueForMetric("ic");
				double cbm = metricDomObj.getValueForMetric("cbm");
				double amc = metricDomObj.getValueForMetric("amc");
				double cc = metricDomObj.getValueForMetric("cc");

				score = Math.min(score, ((10 - noc) - wmc - cbo - lcom3
						+ (2 * cam) - ic - cbm - (0.5 * amc) - cc));

			}
		}
		value = String.valueOf(score);
		return value;
	}

	@Override
	public Color getColorForMetricScore(int index, String className,
			String version) {
		String result = "N/A";
		double minAggregateValue = Integer.MAX_VALUE;

		for (String ver : metricMap.keySet()) {
			for (MetricDOMObject metricDomObj : metricMap.get(ver)) {
				if (metricDomObj != null
						&& metricDomObj.getName().equals(className)) {
					double wmc = metricDomObj.getValueForMetric("wmc");
					double noc = metricDomObj.getValueForMetric("noc");
					double cbo = metricDomObj.getValueForMetric("cbo");
					double lcom3 = metricDomObj.getValueForMetric("lcom3");
					double cam = metricDomObj.getValueForMetric("cam");
					double ic = metricDomObj.getValueForMetric("ic");
					double cbm = metricDomObj.getValueForMetric("cbm");
					double amc = metricDomObj.getValueForMetric("amc");
					double cc = metricDomObj.getValueForMetric("cc");

					switch (index) {
					case 0:
						// All
						result = this.resultLogic.getAggregateValueFor(version,
								className);
						double value = ((10 - noc) - wmc - cbo - lcom3
								+ (2 * cam) - ic - cbm - (0.5 * amc) - cc);
						minAggregateValue = Math.min(minAggregateValue, value);
						break;
					case 1:
						// WMC
						result = this.resultLogic.getMetricValueFor("wmc",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, wmc);
						break;
					case 2:
						// NOC
						result = this.resultLogic.getMetricValueFor("noc",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, noc);
						break;
					case 3:
						// CBO
						result = this.resultLogic.getMetricValueFor("cbo",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, cbo);
						break;
					case 4:
						// LCOM3
						result = this.resultLogic.getMetricValueFor("lcom3",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, lcom3);
						break;
					case 5:
						// CAM
						result = this.resultLogic.getMetricValueFor("cam",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, cam);
						break;
					case 6:
						// CBM
						result = this.resultLogic.getMetricValueFor("cbm",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, cbm);
						break;
					case 7:
						// AMC
						result = this.resultLogic.getMetricValueFor("amc",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, amc);
						break;
					case 8:
						// CC
						result = this.resultLogic.getMetricValueFor("cc",
								version, className);
						minAggregateValue = Math.min(minAggregateValue, cc);
						break;
					default:
						break;
					}
				}
			}
		}

		return getColorForMetric(result, minAggregateValue);
	}

	private Color getColorForMetric(String result, double minAggregateValue) {
		double score = 0;
		try {
			score = Double.parseDouble(result);
		} catch (Exception e) {
			return Color.LIGHT_GRAY;
		}

		//double ratio = 0;
		double spread = this.maxAggregateValue - minAggregateValue;
		double offset = spread / 8;

		double category1 = minAggregateValue + (offset * 1);
		double category2 = minAggregateValue + (offset * 2);
		double category3 = minAggregateValue + (offset * 3);
		double category4 = minAggregateValue + (offset * 4);
		double category5 = minAggregateValue + (offset * 5);
		double category6 = minAggregateValue + (offset * 6);
		double category7 = minAggregateValue + (offset * 7);

		if (score >= minAggregateValue && score < category1) {
			return new Color(255, 0, 0);
		} else if (score >= category1 && score < category2) {
			return new Color(204, 0, 0);
		} else if (score >= category2 && score < category3) {
			return new Color(159, 95, 0);
		} else if (score >= category3 && score < category4) {
			return new Color(255, 102, 0);
		} else if (score >= category4 && score < category5) {
			return new Color(255, 102, 0);
		} else if (score >= category5 && score < category6) {
			return new Color(255, 204, 0);
		} else if (score >= category6 && score < category7) {
			return new Color(95, 159, 0);
		} else {
			return new Color(0, 255, 0);
		}

		// if (this.maxAggregateValue > minAggregateValue) {
		// ratio = (score - minAggregateValue) / spread;
		// }

		// double red = 0;
		// double green = 0;
		//
		// if (ratio < 0.25) {
		// red = 1;
		// green = 6 * ratio;
		// }
		// else if (ratio < 0.5){
		// green = 0.75;
		// red = 1 + 6 * (minAggregateValue - score + 0.25 * spread) / spread;
		// }
		// else if (ratio < 0.75) {
		// green = 1;
		// red = 4 * (score - minAggregateValue - 0.5 * spread) / spread;
		// }
		// else {
		// green = 1;
		// red = 1 + 4 * (minAggregateValue - score + 0.75 * spread) / spread;
		// }
		//
		// int r = (int) Math.max(0, (red * 255) > 255 ? 255 : (red * 255));
		//
		// int g = (int) Math.max(0, (green * 255) > 255 ? 255 : (green * 255));
	}

	@Override
	public void setMaxAggregateValue(double value) {
		this.maxAggregateValue = value;
	}

	@Override
	public double getMaxAggregateValue() {
		return this.maxAggregateValue;
	}
}
