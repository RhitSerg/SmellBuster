package edu.rosehulman.serg.smellbuster.logic;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.rosehulman.serg.smellbuster.metricparser.MetricParserFactory;
import edu.rosehulman.serg.smellbuster.util.MetricDOMObject;

public class ResultTableLogic {

	private Map<String, ArrayList<MetricDOMObject>> metricMap;
	private Map<Integer, String> versionMap;
	private double maxAggregateValue;

	public ResultTableLogic(Map<Integer, String> versionMap) {
		this.metricMap = new HashMap<>();
		this.versionMap = versionMap;
		parseMetrics();
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

	public ArrayList<MetricDOMObject> createClassListMap(String className) {
		ArrayList<MetricDOMObject> list = new ArrayList<MetricDOMObject>();
		MetricDOMObject[] classList = new MetricDOMObject[this.versionMap
				.keySet().size()];
		for (String version : metricMap.keySet()) {
			ArrayList<MetricDOMObject> domObjList = metricMap.get(version);
			for (MetricDOMObject metricDomObj : domObjList) {
				String name = metricDomObj.getName();
				if (name.equals(className)) {

					int j = 0;
					Iterator<Integer> itr = this.versionMap.keySet().iterator();
					while (itr.hasNext()) {
						if (version.equals(this.versionMap.get(itr.next()))) {
							break;
						}
						j++;
					}
					classList[j] = metricDomObj;
				}
			}
		}
		for (int i = 0; i < classList.length; i++) {
			list.add(classList[i]);
		}

		return list;
	}

	public void parseMetrics() {
		File[] files = new File("MetricAnalysis").listFiles();
		MetricParserFactory parser = new MetricParserFactory();

		for (int i = 0; i < files.length; i++) {
			parser.initializeDOMFromFile(files[i].getAbsolutePath());
			parser.parseXML();

			ArrayList<MetricDOMObject> domObjList = parser.getJavaClassList();

			String[] nameParts = files[i].getName().split("-");
			String version = nameParts[nameParts.length - 1];
			version = version.substring(0, version.length() - 4);

			metricMap.put(version, domObjList);
		}
	}

	public String getMetricValueFor(String metric, String version,
			String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (MetricDOMObject metricDomObj : metricMap.get(ver)) {
						if (metricDomObj.getName().equals(className)) {
							value = String.valueOf(metricDomObj
									.getValueForMetric(metric));
						}
					}
				}
			}
		}
		return value;
	}

	public String getAggregateValueFor(String version, String className) {
		String value = "N/A";
		if (version.length() > 0 && className.length() > 0) {
			for (String ver : metricMap.keySet()) {
				if (ver.equals(version)) {
					for (MetricDOMObject metricDomObj : metricMap.get(ver)) {
						if (metricDomObj.getName().equals(className)) {
							value = String.valueOf(metricDomObj.getAggregate());
						}
					}
				}
			}
		}
		return value;
	}

	public String getAggregateMetrics(String className) {
		String value = "N/A";
		double score = 0.0;
		ArrayList<MetricDOMObject> classList = createClassListMap(className);
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

	public String getAggregateMetricsOfMax(String className) {
		String value = "N/A";
		ArrayList<MetricDOMObject> classList = createClassListMap(className);
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

	public String getAggregateMetricsOfMin(String className) {
		String value = "N/A";
		ArrayList<MetricDOMObject> classList = createClassListMap(className);
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
						result = this.getAggregateValueFor(version, className);
						double value = ((10 - noc) - wmc - cbo - lcom3
								+ (2 * cam) - ic - cbm - (0.5 * amc) - cc);
						// maxAggregateValue = Math.max(maxAggregateValue,
						// value);
						minAggregateValue = Math.min(minAggregateValue, value);
						break;
					case 1:
						// WMC
						result = this.getMetricValueFor("wmc", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, wmc);
						minAggregateValue = Math.min(minAggregateValue, wmc);
						break;
					case 2:
						// NOC
						result = this.getMetricValueFor("noc", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, noc);
						minAggregateValue = Math.min(minAggregateValue, noc);
						break;
					case 3:
						// CBO
						result = this.getMetricValueFor("cbo", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, cbo);
						minAggregateValue = Math.min(minAggregateValue, cbo);
						break;
					case 4:
						// LCOM3
						result = this.getMetricValueFor("lcom3", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue,
						// lcom3);
						minAggregateValue = Math.min(minAggregateValue, lcom3);
						break;
					case 5:
						// CAM
						result = this.getMetricValueFor("cam", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, cam);
						minAggregateValue = Math.min(minAggregateValue, cam);
						break;
					case 6:
						// CBM
						result = this.getMetricValueFor("cbm", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, cbm);
						minAggregateValue = Math.min(minAggregateValue, cbm);
						break;
					case 7:
						// AMC
						result = this.getMetricValueFor("amc", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, amc);
						minAggregateValue = Math.min(minAggregateValue, amc);
						break;
					case 8:
						// CC
						result = this.getMetricValueFor("cc", version,
								className);
						// maxAggregateValue = Math.max(maxAggregateValue, cc);
						minAggregateValue = Math.min(minAggregateValue, cc);
						break;
					default:
						break;
					}
				}
			}
		}
		try {
			double score = Double.parseDouble(result);

			double ratio = 0;

			if (this.maxAggregateValue > minAggregateValue)
				ratio = 2 * (score - minAggregateValue)
						/ (this.maxAggregateValue - minAggregateValue);

			double redValue = 255 * (1 - ratio);
			double greenValue = 255 * (ratio - 1);

			int r = (int) Math.max(25, redValue > 255 ? 255 : redValue);// >
																		// 255?
																		// 25:redValue);
			int g = (int) Math.max(25, greenValue > 255 ? 255 : greenValue);// >
																			// 255?
																			// 25:
																			// greenValue);
			int b = 0;
			return new Color(r, g, b);
		} catch (Exception e) {
			return null;
		}
	}

	public void setMaxAggregateValue(double value) {
		this.maxAggregateValue = value;
	}

	public double getMaxAggregateValue() {
		return this.maxAggregateValue;
	}
}
